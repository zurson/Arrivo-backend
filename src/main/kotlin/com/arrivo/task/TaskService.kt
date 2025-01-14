package com.arrivo.task

import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val repository: TaskRepository,
    private val employeeService: EmployeeService
) {
    fun findAll(): List<TaskDTO> = repository.findAll().map { task -> toDto(task) }


    private fun findById(id: Long): Task {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Task with ID $id not found")
        }
    }


    fun findTaskById(id: Long): TaskDTO = toDto(findById(id))


    fun create(request: TaskCreateRequest): TaskDTO {
        val task = Task(
            title = request.title,
            location = request.location,
            addressText = request.addressText,
            status = TaskStatus.UNASSIGNED,
            assignedDate = null,
            employee = null,
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

        task.apply {
            title = request.title
            location = request.location
            addressText = request.addressText
            status = request.status
            assignedDate = if (!isTaskUnassigned(request)) request.assignedDate else null
            employee =
                if (!isTaskUnassigned(request) && request.employeeId != null) employeeService.findById(request.employeeId) else null
        }

        task.products.clear()
        addProductsToTask(
            productsList = request.products,
            task = task,
        )

        return toDto(repository.save(task))
    }


    fun deleteById(id: Long) {
        findById(id).let { repository.deleteById(id) }
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


    private fun isTaskUnassigned(request: TaskUpdateRequest): Boolean {
        return request.status == TaskStatus.UNASSIGNED
    }


    fun getFreeTasks(): List<TaskDTO> {
        return repository.findAllByStatus(TaskStatus.UNASSIGNED).map { toDto(it) }
    }


    private fun toDto(task: Task): TaskDTO {
        return TaskDTO(
            id = task.id,
            title = task.title,
            location = task.location,
            addressText = task.addressText,
            status = task.status,
            assignedDate = task.assignedDate,
            employee = task.employee,
            products = task.products
        )
    }
}