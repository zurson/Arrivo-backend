package com.arrivo.delivery

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@RestController
@RequestMapping("/delivery")
class DeliveryController(private val service: DeliveryService) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAll(): ResponseEntity<List<DeliveryDTO>> {
        return ResponseEntity.ok(service.findAll())
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    fun getByEmployeeId(
        @PathVariable id: Long,
        @RequestParam(required = false) date: LocalDate?
    ): ResponseEntity<DeliveryDTO> {
        val delivery = service.findByEmployeeId(id, date)
        return if (delivery == null) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(delivery)
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/optimize")
    fun getOptimizedRoute(@Valid @RequestBody request: OptimizeRoutesRequest): ResponseEntity<OptimizedRoutesResponse> {
        return ResponseEntity.ok(service.optimizeTours(request))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    fun create(@Valid @RequestBody request: DeliveryCreateRequest): ResponseEntity<DeliveryDTO> {
        return ResponseEntity.ok(service.create(request))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: DeliveryUpdateRequest
    ): ResponseEntity<DeliveryDTO> {
        return ResponseEntity.ok(service.update(id, request))
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{id}")
    fun updateDeliveryStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: DeliveryUpdateStatusRequest
    ): ResponseEntity<DeliveryDTO> {
        return ResponseEntity.ok(service.updateDeliveryStatus(id, request))
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun cancel(@PathVariable id: Long): ResponseEntity<Void> {
        service.cancel(id)
        return ResponseEntity.ok().build()
    }

}
