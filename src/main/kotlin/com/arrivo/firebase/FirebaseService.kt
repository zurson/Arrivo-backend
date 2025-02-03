package com.arrivo.firebase

import com.arrivo.company.Company
import com.arrivo.employee.EmployeeService
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FirebaseService(@Lazy private val employeeService: EmployeeService) {

    private fun getFirebaseUid(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as String
    }


    fun getUserCompany(): Company {
        val firebaseUid = getFirebaseUid()
        val employee = employeeService.findByFirebaseUid(firebaseUid)
        return employee.company
    }


    fun deliveryBelongsToUserCompany(deliveryId: Long): Boolean {
        val company = getUserCompany()
        return company.deliveries.find { it.id == deliveryId } != null
    }


    fun taskBelongsToUserCompany(taskId: Long): Boolean {
        val company = getUserCompany()
        return company.tasks.find { it.id == taskId } != null
    }


    fun employeeBelongsToUserCompany(employeeId: Long): Boolean {
        val company = getUserCompany()
        return company.employees.find { it.id == employeeId } != null
    }

}