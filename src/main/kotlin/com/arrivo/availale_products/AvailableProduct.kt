package com.arrivo.availale_products

import jakarta.persistence.*

@Entity
data class AvailableProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val name: String
)