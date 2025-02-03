package com.arrivo.employee

import com.arrivo.company.CompanyDTO
import com.arrivo.security.Role
import com.arrivo.utilities.capitalize

data class EmployeeDTO(
    val id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    val phoneNumber: String,
    val status: EmployeeStatus,
    val role: Role,
    val company: CompanyDTO
) {
    init {
        firstName = capitalize(firstName.lowercase())
        lastName = capitalize(lastName.lowercase())
        email = email.lowercase()
    }
}