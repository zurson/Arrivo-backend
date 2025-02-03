package com.arrivo.employee;

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/employees")
class EmployeeController(private val service: EmployeeService) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/details")
    fun getUserDetails(): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(service.getUserDetails())
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createAccount(@RequestBody @Valid request: EmployeeCreateAccountRequest): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(service.createAccount(request))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned")
    fun getAllUnassignedEmployeesOnDate(@RequestParam("date") assignedDate: LocalDate): ResponseEntity<List<EmployeeDTO>> {
        val unassignedEmployees = service.getAllEmployeesNotAssignedOnDate(assignedDate)
        return ResponseEntity.ok(unassignedEmployees)
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: EmployeeUpdateAccountRequest): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(service.update(id, request))
    }
}


