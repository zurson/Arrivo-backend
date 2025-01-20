package com.arrivo.employee

import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.firebase.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class EmployeeService(
    private val employeeRepo: EmployeeRepository,
    private val firebaseRepo: FirebaseRepository,
) {
    fun findAll(): List<EmployeeDTO> {
        return employeeRepo.findAll().map { emp ->
            toDTO(emp)
        }
    }


    fun createAccount(request: EmployeeRequest): EmployeeDTO {
        val firebaseUid = firebaseRepo.createFirebaseUser(request.email)

        try {
            val emp = Employee(
                firebaseUid = firebaseUid,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                phoneNumber = request.phoneNumber,
            )

            return toDTO(employeeRepo.save(emp))
        } catch (e: Exception) {
            FirebaseAuth.getInstance().deleteUser(firebaseUid)
            throw e
        }
    }


    fun findById(id: Long): Employee {
        return employeeRepo.findById(id).orElseThrow {
            IdNotFoundException("Employee with ID $id not found")
        }
    }


    fun getAllEmployeesNotAssignedOnDate(date: LocalDate): List<EmployeeDTO> {
        return employeeRepo.findEmployeesNotAssignedOnDate(date).map { emp -> toDTO(emp) }
    }


    fun toDTO(emp: Employee): EmployeeDTO {
        return EmployeeDTO(
            id = emp.id,
            email = emp.email,
            firstName = emp.firstName,
            lastName = emp.lastName,
            phoneNumber = emp.phoneNumber,
            status = emp.status,
        )
    }


    fun update(employeeId: Long, request: EmployeeRequest): EmployeeDTO {
        val employee = findById(employeeId)

        val prevEmail = firebaseRepo.changeUserEmail(
            email = request.email,
            uid = employee.firebaseUid
        )

        if (request.status == EmployeeStatus.FIRED)
            firebaseRepo.blockUserAccount(employee.firebaseUid)
        else if (employee.status == EmployeeStatus.FIRED)
            firebaseRepo.unlockUserAccount(employee.firebaseUid)

        employee.firstName = request.firstName
        employee.lastName = request.lastName
        employee.email = request.email
        employee.phoneNumber = request.phoneNumber
        employee.status = request.status

        try {
            return toDTO(employeeRepo.save(employee))
        } catch (e: Exception) {
            firebaseRepo.changeUserEmail(
                email = prevEmail,
                uid = employee.firebaseUid
            )
            firebaseRepo.unlockUserAccount(employee.firebaseUid)
            throw e
        }

    }

}
