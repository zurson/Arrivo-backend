package com.arrivo.road_accidents

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/road-accidents")
class TrafficEventController(private val service: RoadAccidentService) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAll(): ResponseEntity<List<RoadAccidentDTO>> = ResponseEntity.ok(service.findAll())

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.findAccidentById(id))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun create(@RequestBody request: RoadAccidentCreateRequest): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.create(request))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: RoadAccidentUpdateRequest): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    fun markAsResolved(@PathVariable id: Long): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.markAsResolved(id))
    }

}