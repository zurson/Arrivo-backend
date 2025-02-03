package com.arrivo.delivery

import com.arrivo.company.Company
import com.arrivo.delivery.routes_tracker.DeliveryRoute
import com.arrivo.employee.Employee
import com.arrivo.task.Task
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Delivery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,

    @JsonManagedReference
    @OneToMany
    @OrderColumn(name = "task_order")
    val tasks: MutableList<Task> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "delivery", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val routes: MutableList<DeliveryRoute> = mutableListOf(),

    @Column(nullable = false)
    var timeMinutes: Int = 0,

    @Column(nullable = false)
    var distanceKm: Int = 0,

    @Column(nullable = false)
    var assignedDate: LocalDate,

    @Column(nullable = true)
    var startDate: LocalDateTime? = null,

    @Column(nullable = true)
    var endDate: LocalDateTime? = null,

    @Column(nullable = true)
    var breakDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DeliveryStatus,

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    var employee: Employee
)
