package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.employee.EmployeeService
import com.arrivo.exceptions.CompanyException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.firebase.FirebaseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class RoadAccidentService(
    private val repository: RoadAccidentRepository,
    private val employeeService: EmployeeService,
    private val firebaseService: FirebaseService
) {

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }


    @Transactional
    fun findAll(): List<RoadAccidentDTO> {
        val company = firebaseService.getUserCompany()
        return repository.findAllAccidentsInCompany(company.id).map { roadAccident -> toDto(roadAccident) }
    }


    @Transactional
    fun findAll(id: Long): List<RoadAccidentDTO> {
        val employee = findEmployeeById(id)

        println("SZUKAM WSZYSTKICH DLA PRACOWNIKA ${employee.firstName}")

        if (!firebaseService.employeeBelongsToUserCompany(employee.id))
            throw CompanyException("This employee does not belong to your company")

        println("JEST OKEJ")

        val company = firebaseService.getUserCompany()
        val result = repository.findAllByEmployeeId(employee.id, company.id).map { toDto(it) }

        println("RESULT:")
        result.forEach { r -> println(r) }

        return result
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


    @Transactional
    fun markAsResolved(id: Long): RoadAccidentDTO {
        val accident = findById(id)

        accident.apply {
            status = RoadAccidentStatus.ENDED
        }

        return toDto(repository.save(accident))
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
}