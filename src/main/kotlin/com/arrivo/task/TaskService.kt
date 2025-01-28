package com.arrivo.task

import com.arrivo.delivery.DeliveryStatus
import com.arrivo.exceptions.DataConflictException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TaskService(private val repository: TaskRepository) {


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


    fun updateTaskStatus(id: Long, request: TaskStatusUpdateRequest): TaskDTO {
        val task = findById(id)

        task.apply {
            status = request.status
        }

        return toDto(repository.save(task))
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
            products = task.products
        )
    }


    private fun mapDeliveryStatusToTaskStatus(deliveryStatus: DeliveryStatus?): TaskStatus {
        return when (deliveryStatus) {
            DeliveryStatus.COMPLETED -> TaskStatus.COMPLETED
            DeliveryStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
            DeliveryStatus.ASSIGNED -> TaskStatus.ASSIGNED
            else -> TaskStatus.UNASSIGNED
        }
    }
}