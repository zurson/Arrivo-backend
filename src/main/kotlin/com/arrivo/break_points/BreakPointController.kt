package com.arrivo.break_points

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/break-points")
class StopPointController(private val service: BreakPointService) {
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.findAll())

    @PostMapping
    fun create(@RequestBody request: BreakPointRequest) = ResponseEntity.ok(service.save(request))

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(service.delete(id))
}