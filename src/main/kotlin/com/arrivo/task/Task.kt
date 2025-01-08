package com.arrivo.task

import com.arrivo.employee.Employee
import com.arrivo.task.products.Product
import com.arrivo.utilities.Location
import jakarta.persistence.*
import java.io.Serializable


@Entity
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    var title: String,

    @Embedded
    @Column(nullable = false)
    var location: Location,

    @Column(nullable = false)
    var addressText: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus,

    @ManyToOne
    var employee: Employee?,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "task_id")
    val products: MutableList<Product> = mutableListOf()
) : Serializable
