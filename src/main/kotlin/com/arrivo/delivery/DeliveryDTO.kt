package com.arrivo.delivery

import com.arrivo.employee.EmployeeDTO
import com.arrivo.task.TaskDTO
import java.time.LocalDate

data class DeliveryDTO(
    val id: Long,
    val tasks: List<TaskDTO>,
    var timeMinutes: Int,
    var distanceKm: Int,
    val assignedDate: LocalDate,
    val status: DeliveryStatus,
    val employee: EmployeeDTO
)