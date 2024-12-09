package com.arrivo.task

import com.arrivo.utilities.Location

class TaskUpdateRequest(
    val startLocation: Location,
    val endLocation: Location,
    val distanceKm: Double,
    val cargoWeight: Double,
    val status: TaskStatus,
    val employeeId: Long,
)