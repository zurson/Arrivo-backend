package com.arrivo.task

import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.task.products.Product
import com.arrivo.task.products.ProductRequest
import com.arrivo.task.products.ProductService
import jakarta.transaction.Transactional
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val repository: TaskRepository,
    private val employeeService: EmployeeService,
    private val productService: ProductService,
    private val entityManagerFactory2: LocalContainerEntityManagerFactoryBean,
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


    @Transactional
    fun update(id: Long, request: TaskUpdateRequest): Task {
        val task = findById(id)

        task.apply {
            title = request.title
            location = request.location
            addressText = request.addressText
            status = request.status
            assignedDate = if (!isTaskUnassigned(request)) request.assignedDate else null
            employee = if (!isTaskUnassigned(request) && request.employeeId != null) employeeService.findById(request.employeeId) else null
        }

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


    private fun isTaskUnassigned(request: TaskUpdateRequest): Boolean {
        return request.status == TaskStatus.UNASSIGNED
    }
}