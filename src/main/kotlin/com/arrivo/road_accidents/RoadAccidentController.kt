package com.arrivo.road_accidents

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/road-accidents")
class TrafficEventController(private val service: RoadAccidentService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<RoadAccidentDTO>> = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.findAccidentById(id))
    }

    @PostMapping
    fun create(@RequestBody request: RoadAccidentCreateRequest): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.create(request))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: RoadAccidentUpdateRequest): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @PatchMapping("/{id}")
    fun markAsResolved(@PathVariable id: Long): ResponseEntity<RoadAccidentDTO> {
        return ResponseEntity.ok(service.markAsResolved(id))
    }


//    @DeleteMapping("/{id}")
//    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
//        return ResponseEntity.ok(service.deleteById(id))
//    }

}