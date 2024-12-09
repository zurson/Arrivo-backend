package com.arrivo.utilities

import jakarta.persistence.Embeddable

@Embeddable
data class Location(
    val latitude: Double,
    val longitude: Double
)