package com.arrivo.task

import com.arrivo.employee.Employee
import com.arrivo.utilities.Location
import jakarta.persistence.*

@Entity
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "latitude", column = Column(name = "start_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "start_longitude"))
    )
    var startLocation: Location,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "latitude", column = Column(name = "end_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "end_longitude"))
    )
    var endLocation: Location,

    var distanceKm: Double,
    var cargoWeight: Double,

    @Enumerated(EnumType.STRING)
    var status: TaskStatus = TaskStatus.UNASSIGNED,

    @ManyToOne
    var employee: Employee?
)