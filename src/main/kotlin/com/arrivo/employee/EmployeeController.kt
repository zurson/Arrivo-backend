package com.arrivo.employee;

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(private val service: EmployeeService) {
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Employee> {
        return ResponseEntity.ok(service.findById(id))
    }

    @PostMapping
    fun create(@RequestBody employee: Employee) = ResponseEntity.ok(service.save(employee))

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam status: EmployeeStatus
    ): ResponseEntity<Employee> {
        val updatedEmployee = service.updateStatus(id, status)
        return ResponseEntity.ok(updatedEmployee)
    }
}


