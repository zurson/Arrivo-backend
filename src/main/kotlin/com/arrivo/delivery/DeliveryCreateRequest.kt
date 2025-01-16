package com.arrivo.delivery

import jakarta.validation.Valid
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDate

data class DeliveryCreateRequest(
    @field:NotNull(message = "Tasks list cannot be null")
    @field:Valid
    val tasksIdList: List<DeliveryTask>,

    @field:NotNull(message = "Time cannot be null")
    @field:Positive(message = "Time must be positive")
    val timeMinutes: Int,

    @field:NotNull(message = "Distance cannot be null")
    @field:Positive(message = "Distance must be positive")
    val distanceKm: Int,

    @field:NotNull(message = "Employee ID cannot be null")
    @field:Positive(message = "Employee ID must be positive")
    val employeeId: Long,

    @field:NotNull(message = "Date cannot be null")
    @field:FutureOrPresent(message = "Date cannot be in the past")
    val date: LocalDate,
)
