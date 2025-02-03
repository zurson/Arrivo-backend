package com.arrivo.delivery.routes_tracker

import com.arrivo.utilities.Location
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDateTime

data class TrackPoint(

    @field:NotNull(message = "List cannot be null")
    @field:Valid
    val location: Location,

    @field:NotNull(message = "Timestamp cannot be null")
    @field:PastOrPresent(message = "Timestamp cannot be from the future")
    val timestamp: LocalDateTime,
)
