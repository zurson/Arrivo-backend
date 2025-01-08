package com.arrivo.task

import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.IdNotFoundException
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

    fun create(request: TaskCreationRequest): Task {
        val task = Task(
            startLocation = request.startLocation,
            endLocation = request.endLocation,
            distanceKm = request.distanceKm,
            cargoWeight = request.cargoWeight,
            status = TaskStatus.UNASSIGNED,
            employee = null
        )

        return repository.save(task)
    }

    fun update(id: Long, request: TaskUpdateRequest): Task {
        val task = findById(id)
        val employee = employeeService.findById(request.employeeId)

        task.startLocation = request.startLocation
        task.endLocation = request.endLocation
        task.distanceKm = request.distanceKm
        task.cargoWeight = request.cargoWeight
        task.status = request.status
        task.employee = employee

        return repository.save(task)
    }

    fun deleteById(id: Long) {
        findById(id).let { repository.deleteById(id) }
    }
}