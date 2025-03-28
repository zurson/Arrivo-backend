package com.arrivo.delivery

import com.arrivo.company.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
interface DeliveryRepository : JpaRepository<Delivery, Long> {

    @Query("SELECT COUNT(d) > 0 FROM Delivery d WHERE d.employee.id = :employeeId AND d.assignedDate = :assignedDate")
    fun isEmployeeAssignedOnDate(employeeId: Long, assignedDate: LocalDate): Boolean

    @Query("SELECT d FROM Delivery d WHERE d.employee.id = :employeeId AND d.assignedDate = :assignedDate")
    fun findByEmployeeIdAndAssignedDate(employeeId: Long, assignedDate: LocalDate, companyId: Long): Delivery?

    @Query("SELECT d FROM Delivery d WHERE d.company.id = :companyId")
    fun findAllDeliveriesInCompany(companyId: Long): List<Delivery>

    fun findByAssignedDateBetweenAndStatusAndCompany(
        startDate: LocalDate,
        endDate: LocalDate,
        status: DeliveryStatus,
        company: Company
    ): List<Delivery>

}