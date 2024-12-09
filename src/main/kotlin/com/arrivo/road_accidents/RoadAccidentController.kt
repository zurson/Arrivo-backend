package com.arrivo.road_accidents

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/road-accidents")
class TrafficEventController(private val service: RoadAccidentService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<RoadAccident>> = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RoadAccident> {
        return ResponseEntity.ok(service.findById(id))
    }

    @PostMapping
    fun create(@RequestBody request: RoadAccidentRequest): ResponseEntity<RoadAccident> {
        return ResponseEntity.ok(service.create(request))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: RoadAccidentRequest): ResponseEntity<RoadAccident> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        return ResponseEntity.ok(service.deleteById(id))
    }

}