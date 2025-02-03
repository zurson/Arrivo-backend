package com.arrivo.company

import com.arrivo.utilities.Location

data class CompanyDTO(
    val id: Long,
    val location: Location,
    val name: String,
    val phoneNumber: String
)
