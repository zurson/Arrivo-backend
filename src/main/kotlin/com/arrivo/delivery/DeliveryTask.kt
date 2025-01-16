package com.arrivo.delivery

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class DeliveryTask(
    @field:NotNull(message = "ID cannot be null")
    @field:Positive(message = "ID must be positive")
    val id: Long,
)
