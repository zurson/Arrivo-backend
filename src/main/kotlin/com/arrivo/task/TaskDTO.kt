package com.arrivo.task

import com.arrivo.employee.Employee
import com.arrivo.task.products.Product
import com.arrivo.utilities.Location
import java.time.LocalDate

data class TaskDTO(
    val id: Long,
    var title: String,
    var location: Location,
    var addressText: String,
    var status: TaskStatus,
    var assignedDate: LocalDate?,
    var employee: Employee?,
    val products: List<Product> = listOf()
)