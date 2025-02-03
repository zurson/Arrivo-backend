package com.arrivo.company

import com.arrivo.availale_products.AvailableProduct
import com.arrivo.delivery.Delivery
import com.arrivo.employee.Employee
import com.arrivo.task.Task
import com.arrivo.utilities.Location
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
data class Company(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Embedded
    @Column(nullable = false)
    val location: Location,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val phoneNumber: String,

    // Elements

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    val employees: MutableList<Employee> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    val tasks: MutableList<Task> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    val deliveries: MutableList<Delivery> = mutableListOf(),

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    val availableProducts: MutableList<AvailableProduct> = mutableListOf(),

)
