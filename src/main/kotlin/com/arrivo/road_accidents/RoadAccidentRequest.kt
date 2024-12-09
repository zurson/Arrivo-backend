package com.arrivo.road_accidents

import com.arrivo.utilities.Location
import jakarta.persistence.Embedded
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDate

data class RoadAccidentRequest(
    @Enumerated(EnumType.STRING)
    val status: RoadAccidentStatus = RoadAccidentStatus.NOT_COMPLETED,

    @Embedded
    val location: Location,

    @Enumerated(EnumType.STRING)
    val category: RoadAccidentCategory = RoadAccidentCategory.OTHER,

    val licensePlate: String,
    val date: LocalDate,
    val description: String,
    val employeeId: Long
)