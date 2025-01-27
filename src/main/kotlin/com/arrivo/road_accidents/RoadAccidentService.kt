package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.IdNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class RoadAccidentService(
    private val repository: RoadAccidentRepository,
    private val employeeService: EmployeeService
) {

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }


    fun findAll(): List<RoadAccidentDTO> = repository.findAll().map { roadAccident -> toDto(roadAccident) }


    fun findAll(id: Long): List<RoadAccidentDTO> {
        val employee = findEmployeeById(id)
        return repository.findAllByEmployeeId(employee.id).map { toDto(it) }
    }


    private fun findById(id: Long): RoadAccident {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Road Accident with ID $id not found")
        }
    }


    @Transactional
    fun create(request: RoadAccidentCreateRequest): RoadAccidentDTO {
        val employee = findEmployeeById(request.employeeId)

        val roadAccident = RoadAccident(
            status = RoadAccidentStatus.ACTIVE,
            location = request.location,
            category = request.category,
            licensePlate = request.licensePlate,
            date = request.date,
            description = request.description,
            employee = employee,
        )

        return toDto(repository.save(roadAccident))
    }


    @Transactional
    fun update(id: Long, request: RoadAccidentUpdateRequest): RoadAccidentDTO {
        val roadAccident = findById(id)
        val foundEmployee = findEmployeeById(request.employeeId)

        roadAccident.apply {
            status = request.status
            location = request.location
            category = request.category
            licensePlate = request.licensePlate
            date = request.date
            description = request.description
            employee = foundEmployee
        }

        return toDto(repository.save(roadAccident))
    }


    fun markAsResolved(id: Long): RoadAccidentDTO {
        val accident = findById(id)

        accident.apply {
            status = RoadAccidentStatus.ENDED
        }

        return toDto(repository.save(accident))
    }


    fun deleteById(id: Long) {
        findById(id).let {
            repository.deleteById(id)
        }
    }


    private fun findEmployeeById(id: Long): Employee {
        return employeeService.findById(id)
    }


    private fun toDto(roadAccident: RoadAccident): RoadAccidentDTO {
        return RoadAccidentDTO(
            id = roadAccident.id,
            status = roadAccident.status,
            location = roadAccident.location,
            category = roadAccident.category,
            licensePlate = roadAccident.licensePlate,
            date = roadAccident.date,
            description = roadAccident.description,
            employee = roadAccident.employee,
        )
    }


    private fun formatDate(dateString: String): LocalDate = LocalDate.parse(dateString, DATE_FORMATTER)
}