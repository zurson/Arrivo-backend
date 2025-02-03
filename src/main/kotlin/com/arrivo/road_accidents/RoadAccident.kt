package com.arrivo.road_accidents

import com.arrivo.employee.Employee
import com.arrivo.utilities.Location
import com.fasterxml.jackson.annotation.JsonBackReference
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

    @Column(nullable = false)
    var licensePlate: String,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false)
    var description: String,

    @JsonBackReference
    @ManyToOne
    var employee: Employee
)