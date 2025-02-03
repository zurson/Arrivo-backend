package com.arrivo.delivery

import com.arrivo.company.CompanyService
import com.arrivo.delivery.routes_tracker.DeliveryRoute
import com.arrivo.delivery.routes_tracker.DeliveryRouteDTO
import com.arrivo.delivery.routes_tracker.TrackPointInsertRequest
import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.CompanyException
import com.arrivo.exceptions.DataConflictException
import com.arrivo.exceptions.DeliveryException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.firebase.FirebaseService
import com.arrivo.task.Task
import com.arrivo.task.TaskRepository
import com.arrivo.task.TaskService
import com.arrivo.task.TaskStatus
import com.arrivo.utilities.Location
import com.arrivo.utilities.Settings.Companion.COMPANY_EXCEPTION_ERROR_MESSAGE
import com.arrivo.utilities.Settings.Companion.DELIVERY_ALREADY_COMPLETED_MESSAGE
import com.arrivo.utilities.Settings.Companion.DELIVERY_BREAK_ALREADY_USED
import com.arrivo.utilities.Settings.Companion.DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE
import com.arrivo.utilities.Settings.Companion.DELIVERY_NOT_IN_PROGRESS_MESSAGE
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_AVAILABLE_TIME_HOURS
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_CAR_COST_PER_HOUR
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_CAR_COST_PER_KILOMETER
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_TASK_LABEL_PREFIX
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_TIMEOUT_SECONDS
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_TIME_DEFAULT_START
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_VEHICLE_LABEL
import com.arrivo.utilities.Settings.Companion.TIME_EXCEED_BORDER
import com.arrivo.utilities.Settings.Companion.UNABLE_TO_EDIT_DELIVERY_EMPLOYEE_MESSAGE
import com.arrivo.utilities.Settings.Companion.UNABLE_TO_EDIT_DELIVERY_TASKS_MESSAGE
import com.google.maps.routeoptimization.v1.*
import com.google.maps.routeoptimization.v1.Shipment.VisitRequest
import com.google.protobuf.Timestamp
import com.google.type.LatLng
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.*
import java.util.*
import kotlin.math.ceil


@Service
class DeliveryService(
    @Lazy private val deliveryRepository: DeliveryRepository,
    @Lazy private val taskService: TaskService,
    @Lazy private val employeeService: EmployeeService,
    @Lazy private val taskRepository: TaskRepository,
    @Lazy private val companyService: CompanyService,
    @Lazy private val firebaseService: FirebaseService
) {

    @Value("\${gcp.project.id}")
    private val projectId: String? = null


    private fun findById(id: Long): Delivery {
        return deliveryRepository.findById(id).orElseThrow {
            IdNotFoundException("Delivery with ID $id not found")
        }
    }


    fun findAll(): List<DeliveryDTO> {
        val company = firebaseService.getUserCompany()
        return deliveryRepository.findAllDeliveriesInCompany(company.id).map { delivery -> toDto(delivery) }
    }


    @Transactional
    fun findByEmployeeId(id: Long, date: LocalDate?): DeliveryDTO? {
        val finalDate = date ?: LocalDate.now()
        val company = firebaseService.getUserCompany()
        val employee = employeeService.findById(id)
        val delivery = deliveryRepository.findByEmployeeIdAndAssignedDate(employee.id, finalDate, company.id)

        return if (delivery == null) null else toDto(delivery)
    }


    private fun validateNotCompleted(delivery: Delivery) {
        if (delivery.status == DeliveryStatus.COMPLETED)
            throw DeliveryException(DELIVERY_ALREADY_COMPLETED_MESSAGE)
    }


    @Transactional
    fun finishDelivery(deliveryId: Long) {
        val delivery = findById(deliveryId)

        if (!shouldDeliveryBeFinished(delivery.tasks))
            return

        delivery.apply {
            status = DeliveryStatus.COMPLETED
            endDate = LocalDateTime.now()
        }

        deliveryRepository.save(delivery)
    }


    private fun shouldDeliveryBeFinished(tasks: List<Task>): Boolean {
        return tasks.all { it.status == TaskStatus.COMPLETED }
    }


    private fun validateDeliveryEditable(delivery: Delivery, request: DeliveryUpdateRequest) {
        if (delivery.status == DeliveryStatus.IN_PROGRESS) {
            if (!areAllTasksIdsMatching(delivery.tasks, request.tasksIdList))
                throw DeliveryException(UNABLE_TO_EDIT_DELIVERY_TASKS_MESSAGE)

            if (request.date.toEpochDay() != delivery.assignedDate.toEpochDay())
                throw DeliveryException(UNABLE_TO_EDIT_DELIVERY_EMPLOYEE_MESSAGE)
        }
    }


    private fun validateEmployeeAvailabilityOnDate(employeeId: Long, date: LocalDate) {
        if (deliveryRepository.isEmployeeAssignedOnDate(employeeId, date))
            throw DataConflictException(DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE)
    }


    private fun areDatesTheSame(date1: LocalDate, date2: LocalDate): Boolean = date1.toEpochDay() == date2.toEpochDay()


    private fun clearDeliveryTasks(delivery: Delivery) {
        delivery.tasks.forEach { task ->
            task.apply {
                this.delivery = null
                this.status = TaskStatus.UNASSIGNED
                this.startDate = null
                this.endDate = null
            }

            taskRepository.save(task)
        }

        delivery.tasks.clear()
        deliveryRepository.save(delivery)
    }


    @Transactional
    fun cancel(id: Long) {
        val delivery = findById(id)

        if (!firebaseService.deliveryBelongsToUserCompany(delivery.id))
            throw CompanyException(COMPANY_EXCEPTION_ERROR_MESSAGE)

        validateNotCompleted(delivery)

        clearDeliveryTasks(delivery)
        deliveryRepository.delete(delivery)
    }


    @Transactional
    fun startBreak(id: Long) {
        val delivery = findById(id)

        if (!firebaseService.deliveryBelongsToUserCompany(delivery.id))
            throw CompanyException(COMPANY_EXCEPTION_ERROR_MESSAGE)

        if (delivery.status != DeliveryStatus.IN_PROGRESS)
            throw UnsupportedOperationException(DELIVERY_NOT_IN_PROGRESS_MESSAGE)

        if (delivery.breakDate != null)
            throw UnsupportedOperationException(DELIVERY_BREAK_ALREADY_USED)

        delivery.apply {
            breakDate = LocalDateTime.now()
        }

        deliveryRepository.save(delivery)
    }


    @Transactional
    fun update(id: Long, request: DeliveryUpdateRequest): DeliveryDTO {
        val delivery = findById(id)
        val employee = employeeService.findById(request.employeeId)

        if (!firebaseService.deliveryBelongsToUserCompany(delivery.id))
            throw CompanyException(COMPANY_EXCEPTION_ERROR_MESSAGE)

        if (!areDatesTheSame(delivery.assignedDate, request.date))
            validateEmployeeAvailabilityOnDate(employee.id, request.date)

        validateNotCompleted(delivery)
        validateDeliveryEditable(delivery, request)

        val newTasks = findAllTasks(request.tasksIdList)

        if (!areAllTasksIdsMatching(delivery.tasks, request.tasksIdList)) {
            delivery.tasks.forEach { task ->
                task.delivery = null
                task.status = TaskStatus.UNASSIGNED
                taskRepository.save(task)
            }

            newTasks.forEach { task ->
                task.delivery = delivery
                task.status = TaskStatus.ASSIGNED
                taskRepository.save(task)
            }

            delivery.tasks.clear()
            delivery.tasks.addAll(newTasks)
        }

        delivery.apply {
            this.timeMinutes = request.timeMinutes
            this.distanceKm = request.distanceKm
            this.assignedDate = request.date
            this.employee = employee
        }

        return toDto(deliveryRepository.save(delivery))
    }


    @Transactional
    fun updateDeliveryStatus(deliveryId: Long, request: DeliveryUpdateStatusRequest): DeliveryDTO {
        val delivery = findById(deliveryId)

        if (!firebaseService.deliveryBelongsToUserCompany(delivery.id))
            throw CompanyException(COMPANY_EXCEPTION_ERROR_MESSAGE)

        delivery.apply {
            status = request.status
        }

        return toDto(deliveryRepository.save(delivery))
    }


    private fun areAllTasksIdsMatching(tasks: List<Task>, requestTasks: List<DeliveryTask>): Boolean {
        val deliveryTaskIds = tasks.map { it.id }.toSet()
        val requestTaskIds = requestTasks.map { it.id }.toSet()

        return deliveryTaskIds == requestTaskIds
    }


    private fun findAllTasks(taskIds: List<DeliveryTask>): List<Task> {
        return taskIds.map { deliveryTask -> taskService.findById(deliveryTask.id) }.toList()
    }


    @Transactional
    fun create(request: DeliveryCreateRequest): DeliveryDTO {
        val employee = employeeService.findById(request.employeeId)
        val company = firebaseService.getUserCompany()

        val tasksList: MutableList<Task> = mutableListOf()

        validateEmployeeAvailabilityOnDate(employee.id, request.date)

        request.tasksIdList.forEach { taskId ->
            val task = taskService.findById(taskId.id)
            if (task.delivery != null)
                throw DataConflictException("Task already assigned: ${task.title}")

            tasksList.add(task)
        }

        val delivery = Delivery(
            tasks = tasksList,
            timeMinutes = request.timeMinutes,
            distanceKm = request.distanceKm,
            employee = employee,
            assignedDate = request.date,
            status = DeliveryStatus.ASSIGNED,
            company = company
        )

        val savedDelivery = deliveryRepository.save(delivery)
        tasksList.forEach { task ->
            task.delivery = savedDelivery
            task.status = TaskStatus.ASSIGNED
            taskRepository.save(task)
        }

        company.deliveries.add(savedDelivery)
        companyService.save(company)

        return toDto(savedDelivery)
    }


    fun optimizeTours(optimizeRoutesRequest: OptimizeRoutesRequest): OptimizedRoutesResponse {
        val tasks: List<TaskToOptimize> = optimizeRoutesRequest.tasksToOptimize
        val company = firebaseService.getUserCompany()

        val latitude = company.location.latitude
        val longitude = company.location.longitude

        val clientSettings = RouteOptimizationSettings.newBuilder()
            .setTransportChannelProvider(
                RouteOptimizationSettings.defaultGrpcTransportProviderBuilder()
                    .setKeepAliveTimeDuration(Duration.ofSeconds(30))
                    .build()
            ).build()

        RouteOptimizationClient.create(clientSettings).use { client ->
            val shipments = tasks.map { task ->
                Shipment.newBuilder()
                    .addPickups(
                        VisitRequest.newBuilder()
                            .setArrivalLocation(
                                LatLng.newBuilder()
                                    .setLatitude(task.location.latitude)
                                    .setLongitude(task.location.longitude)
                            )
                            .setLabel("$OPTIMIZATION_TASK_LABEL_PREFIX${task.id}")
                            .build()
                    )
                    .setLabel("$OPTIMIZATION_TASK_LABEL_PREFIX${task.id}")
                    .build()
            }

            val vehicles = listOf(
                Vehicle.newBuilder()
                    .setStartLocation(LatLng.newBuilder().setLatitude(latitude).setLongitude(longitude))
                    .setEndLocation(LatLng.newBuilder().setLatitude(latitude).setLongitude(longitude))
                    .setLabel(OPTIMIZATION_VEHICLE_LABEL)
                    .setCostPerHour(OPTIMIZATION_CAR_COST_PER_HOUR)
                    .setCostPerKilometer(OPTIMIZATION_CAR_COST_PER_KILOMETER)
                    .build()
            )

            val startTime =
                LocalDateTime.of(optimizeRoutesRequest.date, LocalTime.of(OPTIMIZATION_TIME_DEFAULT_START, 0))
            val endTime = LocalDateTime.of(
                optimizeRoutesRequest.date,
                LocalTime.of(OPTIMIZATION_TIME_DEFAULT_START + OPTIMIZATION_AVAILABLE_TIME_HOURS, 0)
            )

            val startInstant = startTime.atZone(ZoneId.of("UTC")).toInstant()
            val endInstant = endTime.atZone(ZoneId.of("UTC")).toInstant()

            val shipmentModel = ShipmentModel.newBuilder()
                .addAllShipments(shipments)
                .addAllVehicles(vehicles)
                .setGlobalStartTime(toTimestamp(startInstant))
                .setGlobalEndTime(toTimestamp(endInstant))

            val request = OptimizeToursRequest.newBuilder()
                .setConsiderRoadTraffic(true)
                .setSearchMode(OptimizeToursRequest.SearchMode.CONSUME_ALL_AVAILABLE_TIME)
                .setParent("projects/$projectId")
                .setTimeout(com.google.protobuf.Duration.newBuilder().setSeconds(OPTIMIZATION_TIMEOUT_SECONDS))
                .setModel(shipmentModel)
                .build()

            val optimizeToursResponse = client.optimizeTours(request)
            return processResponse(optimizeToursResponse, tasks)
        }
    }


    private fun processResponse(
        response: OptimizeToursResponse,
        originalTasks: List<TaskToOptimize>
    ): OptimizedRoutesResponse {
        val totalTimeInMinutes = response.routesList.sumOf { route ->
            route.transitionsList.sumOf { transition -> transition.travelDuration.seconds }
        } / 60

        val totalDistance = response.routesList.sumOf { route ->
            route.transitionsList.sumOf { transition -> transition.travelDistanceMeters }
        } / 1000

        val tasksOrder = LinkedList<TaskToOptimize>()

        response.routesList.forEach { route ->
            route.visitsList.forEach { visit ->
                val label = visit.shipmentLabel
                val matchedTask =
                    originalTasks.firstOrNull { task -> "$OPTIMIZATION_TASK_LABEL_PREFIX${task.id}" == label }
                if (matchedTask != null) {
                    tasksOrder.add(matchedTask)
                }
            }
        }

        tasksOrder.forEach { task ->
            println("Task ID: ${task.id}")
        }
        println("\n")

        val timeInt = totalTimeInMinutes.toInt()

        return OptimizedRoutesResponse(
            timeMinutes = timeInt,
            timeExceeded = isTimeExceeded(timeInMinutes = timeInt),
            distanceKm = ceil(totalDistance).toInt(),
            tasksOrder = tasksOrder
        )
    }


    private fun isTimeExceeded(timeInMinutes: Int): Boolean {
        return timeInMinutes > TIME_EXCEED_BORDER
    }


    private fun toDto(delivery: Delivery): DeliveryDTO {
        return DeliveryDTO(
            id = delivery.id,
            tasks = delivery.tasks.map { task -> taskService.toDto(task) },
            timeMinutes = delivery.timeMinutes,
            distanceKm = delivery.distanceKm,
            assignedDate = delivery.assignedDate,
            startDate = delivery.startDate,
            endDate = delivery.endDate,
            breakDate = delivery.breakDate,
            status = delivery.status,
            employee = employeeService.toDTO(delivery.employee),
        )
    }


    private fun toTimestamp(instant: Instant): Timestamp {
        return Timestamp.newBuilder()
            .setSeconds(instant.toEpochMilli() / 1000)
            .setNanos((((instant.toEpochMilli() % 1000) * 1000000).toInt()))
            .build()
    }


    @Transactional
    fun addRoutePoints(request: TrackPointInsertRequest) {
        val deliveryId = request.deliveryId
        val delivery = findById(deliveryId)

        if (!firebaseService.deliveryBelongsToUserCompany(deliveryId))
            throw CompanyException(COMPANY_EXCEPTION_ERROR_MESSAGE)

        if (delivery.status == DeliveryStatus.COMPLETED)
            throw DeliveryException(DELIVERY_ALREADY_COMPLETED_MESSAGE)

        request.points.forEach { point ->
            val p = DeliveryRoute(
                latitude = point.location.latitude,
                longitude = point.location.longitude,
                timestamp = point.timestamp,
                delivery = delivery
            )

            delivery.routes.add(p)
        }

        deliveryRepository.save(delivery)
    }


    @Transactional
    fun getDeliveryRoutePoints(deliveryId: Long): List<DeliveryRouteDTO> {
        val delivery = findById(deliveryId)

//        if (delivery.status != DeliveryStatus.COMPLETED)
//            throw DeliveryException(DELIVERY_NOT_COMPLETED_MESSAGE)

        return delivery.routes.sortedBy { it.timestamp }.map { toRouteDto(it) }
    }


    private fun toRouteDto(deliveryRoute: DeliveryRoute): DeliveryRouteDTO {
        return DeliveryRouteDTO(
            location = Location(deliveryRoute.latitude, deliveryRoute.longitude),
            timestamp = deliveryRoute.timestamp
        )
    }
}
