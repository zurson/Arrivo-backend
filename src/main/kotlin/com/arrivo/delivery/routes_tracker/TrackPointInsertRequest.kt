package com.arrivo.delivery.routes_tracker

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TrackPointInsertRequest(

    @field:NotNull(message = "List cannot be null")
    @field:Valid
    val points: List<TrackPoint>,

    @field:NotNull(message = "Delivery ID cannot be null")
    @field:Positive(message = "Delivery ID must be positive")
    val deliveryId: Long

)