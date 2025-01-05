package com.arrivo.employee

import com.arrivo.utilities.capitalize

data class EmployeeDTO(
    val id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var status: EmployeeStatus,
) {
    init {
        firstName = capitalize(firstName.lowercase())
        lastName = capitalize(lastName.lowercase())
        email = email.lowercase()
    }
}