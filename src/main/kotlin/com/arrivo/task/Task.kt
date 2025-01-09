package com.arrivo.task

import com.arrivo.employee.Employee
import com.arrivo.task.products.Product
import com.arrivo.utilities.Location
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime


@Entity
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Embedded
    @Column(nullable = false)
    var location: Location,

    @Column(nullable = false)
    var addressText: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus,

    var assignedDate: LocalDateTime?,

    @ManyToOne
    var employee: Employee?,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "task_id")
    val products: MutableList<Product> = mutableListOf()
) : Serializable
