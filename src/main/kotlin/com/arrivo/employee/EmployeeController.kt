package com.arrivo.employee;

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(private val service: EmployeeService) {
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(service.findEmployeeById(id))
    }

    @PostMapping
    fun createAccount(@RequestBody @Valid request: EmployeeRequest): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(service.createAccount(request))
    }


    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: EmployeeRequest
    ) {
        service.update(id, request)
    }
}


