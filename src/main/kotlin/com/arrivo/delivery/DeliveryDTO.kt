package com.arrivo.delivery

import com.arrivo.task.TaskDTO

data class DeliveryDTO(
    val id: Long,
    val tasks: List<TaskDTO>,
    var timeMinutes: Int,
    var distanceKm: Int
)