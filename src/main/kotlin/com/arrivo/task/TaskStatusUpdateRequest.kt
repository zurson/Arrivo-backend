package com.arrivo.task

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotNull

data class TaskStatusUpdateRequest(
    @Enumerated(EnumType.STRING)
    @field:NotNull(message = "Task status cannot be null")
    val status: TaskStatus,
)
