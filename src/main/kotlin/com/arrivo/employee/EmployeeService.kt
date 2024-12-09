package com.arrivo.employee

import com.arrivo.utilities.IdNotFoundException
import com.arrivo.utilities.InvalidStatusException
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val repository: EmployeeRepository) {
    fun findAll(): List<Employee> = repository.findAll()

    fun save(employee: Employee) {
        repository.save(employee)
    }

    fun findById(id: Long): Employee {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Employee with ID $id not found")
        }
    }

    fun updateStatus(employeeId: Long, status: EmployeeStatus): Employee {
        val employee = findById(employeeId)

        employee.status = status
        return repository.save(employee)
    }

}
