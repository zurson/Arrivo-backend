package com.arrivo.delivery

import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.DataConflictException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.Task
import com.arrivo.task.TaskRepository
import com.arrivo.task.TaskService
import com.arrivo.utilities.Settings.Companion.DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_AVAILABLE_TIME_HOURS
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_TASK_LABEL_PREFIX
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_TIME_DEFAULT_START
import com.arrivo.utilities.Settings.Companion.OPTIMIZATION_VEHICLE_LABEL
import com.arrivo.utilities.Settings.Companion.TIME_EXCEED_BORDER
import com.google.maps.routeoptimization.v1.*
import com.google.maps.routeoptimization.v1.Shipment.VisitRequest
import com.google.protobuf.Timestamp
import com.google.type.LatLng
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.*
import java.util.*
import kotlin.math.ceil


@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val taskService: TaskService,
    private val employeeService: EmployeeService,
    private val taskRepository: TaskRepository
) {

    @Value("\${gcp.project.id}")
    private val projectId: String? = null

    private fun findById(id: Long): Delivery {
        return deliveryRepository.findById(id).orElseThrow {
            IdNotFoundException("Delivery with ID $id not found")
        }
    }


    fun findAll(): List<DeliveryDTO> = deliveryRepository.findAll().map { delivery -> toDto(delivery) }


    @Transactional
    fun create(request: DeliveryCreateRequest): DeliveryDTO {
        val employee = employeeService.findById(request.employeeId)
        val tasksList: MutableList<Task> = mutableListOf()

        if (deliveryRepository.isEmployeeAssignedOnDate(employee.id, request.date))
            throw DataConflictException(DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE)

        request.tasksIdList.forEach { taskId ->
            val task = taskService.findById(taskId.id)
            if (task.delivery != null)
                throw DataConflictException("Task already assigned: ${task.title}")

            tasksList.add(task)
        }

        var delivery = Delivery(
            tasks = tasksList,
            timeMinutes = request.timeMinutes,
            distanceKm = request.distanceKm,
            employee = employee,
            assignedDate = request.date,
            status = DeliveryStatus.ASSIGNED,
        )

        val deliveryDTO = toDto(deliveryRepository.save(delivery))
        delivery = findById(deliveryDTO.id)

        tasksList.forEach { task ->
            task.delivery = delivery
            taskRepository.save(task)
        }

        return deliveryDTO
    }


    fun optimizeTours(optimizeRoutesRequest: OptimizeRoutesRequest): OptimizedRoutesResponse {
        val tasks: List<TaskToOptimize> = optimizeRoutesRequest.tasksToOptimize

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
                    .setStartLocation(LatLng.newBuilder().setLatitude(52.1234).setLongitude(22.1234))
                    .setEndLocation(LatLng.newBuilder().setLatitude(52.1234).setLongitude(22.1234))
                    .setLabel(OPTIMIZATION_VEHICLE_LABEL)
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
}
