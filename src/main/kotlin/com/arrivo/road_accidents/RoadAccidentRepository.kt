package com.arrivo.road_accidents

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface RoadAccidentRepository : JpaRepository<RoadAccident, Long> {

    @Query("SELECT r FROM RoadAccident r WHERE r.employee.id = :employeeId")
    fun findAllByEmployeeId(employeeId: Long): List<RoadAccident>

    @Query("SELECT r FROM RoadAccident r WHERE r.employee.company.id = :companyId")
    fun findAllAccidentsInCompany(companyId: Long): List<RoadAccident>
}