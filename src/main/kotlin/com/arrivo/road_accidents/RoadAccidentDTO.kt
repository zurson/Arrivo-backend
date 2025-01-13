package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.utilities.Location
import java.time.LocalDate

data class RoadAccidentDTO (
    val id: Long,
    val status: RoadAccidentStatus,
    val location: Location,
    val category: RoadAccidentCategory,
    val licensePlate: String,
    val date: LocalDate,
    val description: String,
    val employee: Employee
)