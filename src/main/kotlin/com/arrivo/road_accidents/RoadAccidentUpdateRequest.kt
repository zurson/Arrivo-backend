package com.arrivo.road_accidents

import com.arrivo.utilities.Location
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Embedded
import jakarta.validation.constraints.*
import java.time.LocalDate

data class RoadAccidentUpdateRequest(

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    val status: RoadAccidentStatus,

    @Embedded
    @NotNull(message = "Location cannot be null")
    val location: Location,

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category cannot be null")
    val category: RoadAccidentCategory,

    @NotBlank(message = "License plate cannot be blank")
    @Size(min = 1, max = 15, message = "License plate must be between 1 and 15 characters")
    val licensePlate: String,

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date must be in the past or present")
    val date: LocalDate,

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 250, message = "Description cannot exceed 500 characters")
    val description: String,

    @NotNull(message = "Employee ID cannot be null")
    @Positive(message = "Employee ID must be a positive number")
    val employeeId: Long
)
