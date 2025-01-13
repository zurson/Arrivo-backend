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
    var status: RoadAccidentStatus = RoadAccidentStatus.ACTIVE,

    @Embedded
    var location: Location,

    @Enumerated(EnumType.STRING)
    var category: RoadAccidentCategory = RoadAccidentCategory.OTHER,

    var licensePlate: String,
    var date: LocalDate,
    var description: String,

    @ManyToOne
    var employee: Employee
)