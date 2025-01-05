package com.arrivo.employee

import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
data class Employee(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val firebaseUid: String,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Email
    @Column(unique = true, nullable = false)
    var email: String,

    @Column(unique = true, nullable = false)
    var phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: EmployeeStatus = EmployeeStatus.HIRED,
)