package com.arrivo.availale_products

import com.arrivo.company.Company
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class AvailableProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val name: String,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,
)