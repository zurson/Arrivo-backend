package com.arrivo.task

import com.arrivo.utilities.Location

data class TaskCreationRequest(
    val startLocation: Location,
    val endLocation: Location,
    val distanceKm: Double,
    val cargoWeight: Double,
)