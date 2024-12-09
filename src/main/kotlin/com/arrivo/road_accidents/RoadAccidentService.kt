package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.employee.EmployeeService
import com.arrivo.utilities.IdNotFoundException
import org.springframework.stereotype.Service

@Service
class RoadAccidentService(
    private val repository: RoadAccidentRepository,
    private val employeeService: EmployeeService
) {

    fun findAll(): List<RoadAccident> = repository.findAll()

    fun findById(id: Long): RoadAccident {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Road Accident with ID $id not found")
        }
    }

    fun create(request: RoadAccidentRequest): RoadAccident {
        val employee = findEmployeeById(request.employeeId)

        val roadAccident = RoadAccident(
            status = request.status,
            location = request.location,
            category = request.category,
            licensePlate = request.licensePlate,
            date = request.date,
            description = request.description,
            employee = employee,
        )

        return repository.save(roadAccident)
    }

    fun update(id: Long, updatedRoadAccident: RoadAccidentRequest): RoadAccident {
        val existingEvent = findById(id)
        val employee = findEmployeeById(updatedRoadAccident.employeeId)

        val eventToSave = existingEvent.copy(
            status = updatedRoadAccident.status,
            location = updatedRoadAccident.location,
            category = updatedRoadAccident.category,
            licensePlate = updatedRoadAccident.licensePlate,
            date = updatedRoadAccident.date,
            description = updatedRoadAccident.description,
            employee = employee
        )

        return repository.save(eventToSave)
    }

    fun deleteById(id: Long) {
        findById(id).let {
            repository.deleteById(id)
        }
    }

    private fun findEmployeeById(id: Long): Employee {
        return employeeService.findById(id)
    }
}