package com.arrivo.delivery

import com.arrivo.employee.Employee
import com.arrivo.task.Task
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Delivery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn(name = "task_order")
    val tasks: MutableList<Task> = mutableListOf(),

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    var employee: Employee
)
