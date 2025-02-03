package com.arrivo.employee

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class EmployeeCreateAccountRequest(
    @field:NotBlank(message = "First name cannot be blank")
    @field:Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    var firstName: String,

    @field:NotBlank(message = "Last name cannot be blank")
    @field:Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    var lastName: String,

    @field:Email(message = "Email improper format")
    var email: String,

    @field:NotBlank(message = "Phone number cannot be blank")
    @field:Pattern(
        regexp = "^[0-9]{9}$",
        message = "Phone number must consist of exactly 9 digits"
    )
    val phoneNumber: String,

    @field:NotNull(message = "Company ID cannot be null")
    @field:Positive(message = "Company ID must be positive")
    val companyId: Long

    ) {
    init {
        firstName = firstName.uppercase()
        lastName = lastName.uppercase()
        email = email.uppercase()
    }
}
