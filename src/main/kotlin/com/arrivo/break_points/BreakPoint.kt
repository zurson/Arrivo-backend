package com.arrivo.break_points

import com.arrivo.utilities.Location
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["latitude", "longitude"])])
data class BreakPoint(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Embedded
    val location: Location

)