package com.arrivo.employee

import com.arrivo.company.CompanyService
import com.arrivo.exceptions.CompanyException
import com.arrivo.exceptions.IdNotFoundException
import com.arrivo.firebase.FirebaseRepository
import com.arrivo.firebase.FirebaseService
import com.arrivo.security.Role
import com.arrivo.utilities.Settings.Companion.USER_NOT_FOUND_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class EmployeeService(
    @Lazy private val employeeRepository: EmployeeRepository,
    @Lazy private val firebaseRepo: FirebaseRepository,
    @Lazy private val firebaseService: FirebaseService,
    @Lazy private val companyService: CompanyService,
) {

    fun findAll(): List<EmployeeDTO> {
        val company = firebaseService.getUserCompany()
        return employeeRepository.findAllEmployeesInCompany(company.id)
            .filter { emp -> emp.role != Role.ADMIN }
            .map { emp -> toDTO(emp) }

    }


    @Transactional
    fun createAccount(request: EmployeeCreateAccountRequest): EmployeeDTO {
        val firebaseUid = firebaseRepo.createFirebaseUser(request.email)
        val company = companyService.findById(request.companyId)

        try {
            val employee = Employee(
                firebaseUid = firebaseUid,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                phoneNumber = request.phoneNumber,
                company = company
            )

            company.employees.add(employee)
            companyService.save(company)

            return toDTO(employeeRepository.save(employee))
        } catch (e: Exception) {
            FirebaseAuth.getInstance().deleteUser(firebaseUid)
            throw e
        }
    }


    fun findById(id: Long): Employee {
        return employeeRepository.findById(id).orElseThrow {
            IdNotFoundException("Employee with ID $id not found")
        }
    }


    fun findByFirebaseUid(uid: String): Employee {
        return employeeRepository.findByFirebaseUid(uid).orElseThrow {
            IdNotFoundException("Employee with UID $uid not found")
        }
    }


    fun getAllEmployeesNotAssignedOnDate(date: LocalDate): List<EmployeeDTO> {
        val company = firebaseService.getUserCompany()
        return employeeRepository.findEmployeesNotAssignedOnDate(date, company.id).map { emp -> toDTO(emp) }
    }


    fun toDTO(employee: Employee): EmployeeDTO {
        return EmployeeDTO(
            id = employee.id,
            email = employee.email,
            firstName = employee.firstName,
            lastName = employee.lastName,
            phoneNumber = employee.phoneNumber,
            status = employee.status,
            role = employee.role,
            company = companyService.toDto(employee.company)
        )
    }


    @Transactional
    fun update(employeeId: Long, request: EmployeeUpdateAccountRequest): EmployeeDTO {
        val employee = findById(employeeId)

        if (!firebaseService.employeeBelongsToUserCompany(employee.id))
            throw CompanyException("This employee does not belong to your company")

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
            return toDTO(employeeRepository.save(employee))
        } catch (e: Exception) {
            firebaseRepo.changeUserEmail(
                email = prevEmail,
                uid = employee.firebaseUid
            )
            firebaseRepo.unlockUserAccount(employee.firebaseUid)
            throw e
        }

    }


    fun getUserDetails(): EmployeeDTO {
        val authentication = SecurityContextHolder.getContext().authentication
        val firebaseUid = authentication.principal as String

        val employee = employeeRepository.findByFirebaseUid(firebaseUid).orElseThrow {
            throw Exception(USER_NOT_FOUND_MESSAGE)
        }

        return toDTO(employee)
    }


}
