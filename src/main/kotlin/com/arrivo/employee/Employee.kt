package com.arrivo.employee

import com.arrivo.company.Company
import com.arrivo.security.Role
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
data class Employee(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER
)