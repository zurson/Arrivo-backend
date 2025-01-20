package com.arrivo.break_points

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/break-points")
class StopPointController(private val service: BreakPointService) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun create(@RequestBody request: BreakPointRequest) = ResponseEntity.ok(service.save(request))

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(service.delete(id))
}