package com.arrivo.task

import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val repository: TaskRepository,
    private val employeeService: EmployeeService,
) {
    fun findAll(): List<Task> = repository.findAll()


    fun findById(id: Long): Task {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Task with ID $id not found")
        }
    }


    fun create(request: TaskCreateRequest): Task {
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

        return repository.save(task)
    }


    fun update(id: Long, request: TaskUpdateRequest): Task {
        val task = findById(id)
        val employee = employeeService.findById(request.employeeId)

        task.title = request.title
        task.location = request.location
        task.addressText = request.addressText
        task.status = request.status
        task.assignedDate = request.assignedDate
        task.employee = employee

        task.products.clear()
        addProductsToTask(
            productsList = request.products,
            task = task,
        )

        return repository.save(task)
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
}