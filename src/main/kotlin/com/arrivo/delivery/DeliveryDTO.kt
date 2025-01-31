package com.arrivo.delivery

import com.arrivo.employee.EmployeeDTO
import com.arrivo.task.TaskDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class DeliveryDTO(
    val id: Long,
    val tasks: List<TaskDTO>,
    var timeMinutes: Int,
    var distanceKm: Int,
    val assignedDate: LocalDate,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val breakDate: LocalDateTime?,
    val status: DeliveryStatus,
    val employee: EmployeeDTO
)