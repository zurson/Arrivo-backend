package com.arrivo.employee

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.*

data class EmployeeUpdateAccountRequest(
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

    @Enumerated(EnumType.STRING)
    @field:NotNull(message = "Employee status cannot be null")
    val status: EmployeeStatus

) {
    init {
        firstName = firstName.uppercase()
        lastName = lastName.uppercase()
        email = email.uppercase()
    }
}