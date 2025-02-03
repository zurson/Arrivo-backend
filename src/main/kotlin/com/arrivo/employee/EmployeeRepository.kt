package com.arrivo.employee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*


interface EmployeeRepository : JpaRepository<Employee, Long> {

    @Query(
        """
    SELECT e FROM Employee e
    WHERE e.company.id = :companyId 
    AND e.id NOT IN (
        SELECT d.employee.id FROM Delivery d WHERE d.assignedDate = :assignedDate
    )
    """
    )
    fun findEmployeesNotAssignedOnDate(assignedDate: LocalDate, companyId: Long): List<Employee>


    fun findByFirebaseUid(firebaseUid: String): Optional<Employee>

    @Query("SELECT e FROM Employee e WHERE e.company.id = :companyId")
    fun findAllEmployeesInCompany(companyId: Long): List<Employee>
}
