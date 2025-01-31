package com.arrivo.task

import com.arrivo.delivery.DeliveryRepository
import com.arrivo.delivery.DeliveryService
import com.arrivo.delivery.DeliveryStatus
import com.arrivo.exceptions.DataConflictException
import com.arrivo.exceptions.DataCorruptedException
import com.arrivo.exceptions.DeliveryNotEditableException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import com.arrivo.utilities.Settings.Companion.DELIVERY_ALREADY_COMPLETED_MESSAGE
import com.arrivo.utilities.Settings.Companion.NO_DELIVERY_ASSIGNED
import com.arrivo.utilities.Settings.Companion.TASK_ALREADY_COMPLETED_MESSAGE
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaskService(
    private val repository: TaskRepository,
    private val deliveryService: DeliveryService,
    @Lazy private val deliveryRepository: DeliveryRepository
) {

    fun findAll(): List<TaskDTO> = repository.findAll().map { task -> toDto(task) }


    fun findById(id: Long): Task {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Task with ID $id not found")
        }
    }


    fun create(request: TaskCreateRequest): TaskDTO {
        val task = Task(
            title = request.title,
            location = request.location,
            addressText = request.addressText,
            status = TaskStatus.UNASSIGNED,
            products = mutableListOf()
        )

        addProductsToTask(
            productsList = request.products,
            task = task,
        )

        return toDto(repository.save(task))
    }


    @Transactional
    fun update(id: Long, request: TaskUpdateRequest): TaskDTO {
        val task = findById(id)

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
        return repository.findAllByDeliveryIsNull().map { task -> toDto(task) }
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