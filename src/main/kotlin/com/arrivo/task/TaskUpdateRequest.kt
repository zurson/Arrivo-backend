package com.arrivo.task

import com.arrivo.task.products.ProductRequest
import com.arrivo.utilities.Location
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class TaskUpdateRequest(

    @field:NotBlank(message = "Title cannot be blank")
    @field:Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    val title: String,

    @field:NotNull(message = "Location cannot be null")
    @field:Valid
    val location: Location,

    @field:NotBlank(message = "Address text cannot be blank")
    val addressText: String,

    @field:NotNull(message = "Status cannot be null")
    val status: TaskStatus,

    @field:FutureOrPresent(message = "Assigned date cannot be in the past")
    val assignedDate: LocalDateTime?,

    @field:Positive(message = "Employee ID must be a positive number")
    val employeeId: Long?,

    @field:NotEmpty(message = "Products list cannot be empty")
    @field:Valid
    val products: List<ProductRequest>
)