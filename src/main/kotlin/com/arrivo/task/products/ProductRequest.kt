package com.arrivo.task.products

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(min = 1, max = 50, message = "Product name must be between 1 and 50 characters")
    val name: String,

    @field:Positive(message = "Product amount must be positive")
    val amount: Int
)