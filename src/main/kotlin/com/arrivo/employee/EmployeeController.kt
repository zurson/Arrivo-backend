package com.arrivo.employee;

import com.arrivo.security.Role
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


    @GetMapping("/role")
    fun getUserRole(): ResponseEntity<Role> {
        return ResponseEntity.ok(service.getUserRole())
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun createAccount(@RequestBody @Valid request: EmployeeRequest): ResponseEntity<EmployeeDTO> {
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
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: EmployeeRequest
    ) {
        service.update(id, request)
    }
}


