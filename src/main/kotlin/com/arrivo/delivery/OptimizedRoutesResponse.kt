package com.arrivo.delivery

import java.util.*

data class OptimizedRoutesResponse(
    val timeMinutes: Int,
    val timeExceeded: Boolean,
    val distanceKm: Int,
    val tasksOrder: LinkedList<TaskToOptimize>
)