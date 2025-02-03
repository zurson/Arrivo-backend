package com.arrivo.task

import com.arrivo.company.CompanyService
import com.arrivo.delivery.DeliveryRepository
import com.arrivo.delivery.DeliveryService
import com.arrivo.delivery.DeliveryStatus
import com.arrivo.exceptions.*
import com.arrivo.firebase.FirebaseService
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import com.arrivo.utilities.Settings.Companion.DELIVERY_ALREADY_COMPLETED_MESSAGE
import com.arrivo.utilities.Settings.Companion.NO_DELIVERY_ASSIGNED
import com.arrivo.utilities.Settings.Companion.TASK_ALREADY_COMPLETED_MESSAGE
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TaskService(
    private val repository: TaskRepository,
    private val deliveryService: DeliveryService,
    @Lazy private val deliveryRepository: DeliveryRepository,
    private val firebaseService: FirebaseService,
    private val companyService: CompanyService
) {

    fun findAll(): List<TaskDTO> {
        val company = firebaseService.getUserCompany()
        return repository.findAllTasksInCompany(company.id).map { task -> toDto(task) }
    }


    fun findById(id: Long): Task {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Task with ID $id not found")
        }
    }


    @Transactional
    fun create(request: TaskCreateRequest): TaskDTO {
        val company = firebaseService.getUserCompany()

        val task = Task(
            title = request.title,
            location = request.location,
            addressText = request.addressText,
            status = TaskStatus.UNASSIGNED,
            products = mutableListOf(),
            company = company
        )

        addProductsToTask(
            productsList = request.products,
            task = task,
        )

        company.tasks.add(task)
        companyService.save(company)

        return toDto(repository.save(task))
    }


    @Transactional
    fun update(id: Long, request: TaskUpdateRequest): TaskDTO {
        val task = findById(id)

        if (!firebaseService.taskBelongsToUserCompany(task.id))
            throw CompanyException("This task does not belong to your company")

        if (task.delivery != null)
            throw DataConflictException("Task already assigned")

        task.apply {
            title = request.title
            location = request.location
            addressText = request.addressText
        }

        task.products.clear()
        addProductsToTask(
            productsList = request.products,
            task = task,
        )

        return toDto(repository.save(task))
    }


    @Transactional
    fun updateTaskStatus(id: Long, request: TaskStatusUpdateRequest): TaskDTO {
        var task = findById(id)

        if (!firebaseService.taskBelongsToUserCompany(task.id))
            throw CompanyException("This task does not belong to your company")

        if (task.delivery == null)
            throw DataCorruptedException(NO_DELIVERY_ASSIGNED)

        if (task.delivery!!.status == DeliveryStatus.COMPLETED)
            throw DeliveryNotEditableException(DELIVERY_ALREADY_COMPLETED_MESSAGE)

        if (task.status == TaskStatus.COMPLETED)
            throw UnsupportedOperationException(TASK_ALREADY_COMPLETED_MESSAGE)

        if (task.status != TaskStatus.IN_PROGRESS && request.status == TaskStatus.IN_PROGRESS)
            task.apply { startDate = LocalDateTime.now() }

        if (task.status != TaskStatus.COMPLETED && request.status == TaskStatus.COMPLETED)
            task.apply { endDate = LocalDateTime.now() }

        if (task.delivery!!.status != DeliveryStatus.IN_PROGRESS) {
            task.delivery!!.apply {
                status = DeliveryStatus.IN_PROGRESS
                startDate = LocalDateTime.now()
            }
            deliveryRepository.save(task.delivery!!)
        }

        task.apply { status = request.status }

        task = repository.save(task)
        deliveryService.finishDelivery(task.delivery!!.id)

        return toDto(task)
    }


    private fun addProductsToTask(productsList: List<ProductRequest>, task: Task) {
        productsList.forEach { product ->
            task.products.add(
                Product(
                    name = product.name,
                    amount = product.amount,
                )
            )
        }
    }


    fun getFreeTasks(): List<TaskDTO> {
        val company = firebaseService.getUserCompany()
        return repository.findAllByDeliveryIsNull(company.id).map { task -> toDto(task) }
    }


    fun toDto(task: Task): TaskDTO {
        return TaskDTO(
            id = task.id,
            title = task.title,
            location = task.location,
            addressText = task.addressText,
            status = task.status,
            assignedDate = task.delivery?.assignedDate,
            employee = task.delivery?.employee,
            products = task.products,
            startDate = task.startDate,
            endDate = task.endDate
        )
    }
}