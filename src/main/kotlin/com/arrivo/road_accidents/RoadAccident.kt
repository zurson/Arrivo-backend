package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.utilities.Location
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class RoadAccident(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val status: RoadAccidentStatus = RoadAccidentStatus.NOT_COMPLETED,

    @Embedded
    val location: Location,

    @Enumerated(EnumType.STRING)
    val category: RoadAccidentCategory = RoadAccidentCategory.OTHER,

    val licensePlate: String,
    val date: LocalDate,
    val description: String,

    @ManyToOne
    val employee: Employee
)