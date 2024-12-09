package com.arrivo.employee

import jakarta.persistence.*

@Entity
data class Employee(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val firstName: String,
    val lastName: String,

    @Enumerated(EnumType.STRING)
    var status: EmployeeStatus = EmployeeStatus.HIRED,
)