package com.arrivo.delivery

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotNull

data class DeliveryUpdateStatusRequest(
    @Enumerated(EnumType.STRING)
    @field:NotNull(message = "Delivery status cannot be null")
    val status: DeliveryStatus,
)
