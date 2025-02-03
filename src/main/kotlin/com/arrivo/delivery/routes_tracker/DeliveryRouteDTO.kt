package com.arrivo.delivery.routes_tracker

import com.arrivo.utilities.Location
import java.time.LocalDateTime

data class DeliveryRouteDTO(
    val location: Location,
    val timestamp: LocalDateTime
)