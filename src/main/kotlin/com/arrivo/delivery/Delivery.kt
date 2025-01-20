package com.arrivo.delivery

import com.arrivo.employee.Employee
import com.arrivo.task.Task
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Delivery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToMany
    @OrderColumn(name = "task_order")
    val tasks: MutableList<Task> = mutableListOf(),

    @Column(nullable = false)
    var timeMinutes: Int = 0,

    @Column(nullable = false)
    var distanceKm: Int = 0,

    @Column(nullable = false)
    var assignedDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DeliveryStatus,

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    var employee: Employee
)
