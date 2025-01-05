package com.arrivo.exceptions

data class ErrorResponseDTO(
    val code: Int,
    val errors: List<String> = emptyList()
)