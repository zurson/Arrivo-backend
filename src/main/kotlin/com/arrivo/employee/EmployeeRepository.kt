package com.arrivo.employee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate


interface EmployeeRepository : JpaRepository<Employee, Long> {

    @Query(
        """
        SELECT e FROM Employee e 
        WHERE e.id NOT IN (
            SELECT d.employee.id FROM Delivery d WHERE d.assignedDate = :assignedDate
        )
    """
    )
    fun findEmployeesNotAssignedOnDate(assignedDate: LocalDate): List<Employee>

    fun findByFirebaseUid(firebaseUid: String): Employee?
}
