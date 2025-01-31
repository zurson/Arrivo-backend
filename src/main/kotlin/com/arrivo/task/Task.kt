package com.arrivo.task

import com.arrivo.delivery.Delivery
import com.arrivo.task.products.Product
import com.arrivo.utilities.Location
import jakarta.persistence.*
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

    @Column(nullable = true)
    var startDate: LocalDateTime? = null,

    @Column(nullable = true)
    var endDate: LocalDateTime? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "task_id")
    val products: MutableList<Product> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = true)
    var delivery: Delivery? = null

)
