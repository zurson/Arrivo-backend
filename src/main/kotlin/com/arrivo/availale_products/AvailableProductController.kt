package com.arrivo.availale_products

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/available-products")
class AvailableProductController(
    private val availableProductService: AvailableProductService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAllAvailableProducts(): ResponseEntity<List<AvailableProductDTO>> {
        return ResponseEntity.ok(availableProductService.getAllAvailableProducts())
    }
}