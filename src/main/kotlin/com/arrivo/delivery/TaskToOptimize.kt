package com.arrivo.delivery

import com.arrivo.utilities.Location
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TaskToOptimize(
    @field:NotNull(message = "ID cannot be null")
    @field:Positive(message = "ID must be positive")
    val id: Long,

    @field:NotNull(message = "Name cannot be null")
    @field:Valid
    val location: Location,
)