package com.arrivo.delivery

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import java.time.LocalDate

data class OptimizeRoutesRequest(
    @field:NotEmpty(message = "Locations list cannot be empty")
    @field:Valid
    val tasksToOptimize: List<TaskToOptimize>,
    val date: LocalDate
)