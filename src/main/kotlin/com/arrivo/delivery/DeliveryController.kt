package com.arrivo.delivery

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/delivery")
class DeliveryController(private val service: DeliveryService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<DeliveryDTO>> {
        return ResponseEntity.ok(service.findAll())
    }


    @PostMapping("/optimize")
    fun getOptimizedRoute(@Valid @RequestBody request: OptimizeRoutesRequest): ResponseEntity<OptimizedRoutesResponse> {
        return ResponseEntity.ok(service.optimizeTours(request))
    }


    @PostMapping("/create")
    fun create(@Valid @RequestBody request: DeliveryCreateRequest): ResponseEntity<DeliveryDTO> {
        return ResponseEntity.ok(service.create(request))
    }
}
