package com.arrivo.availale_products

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/available-products")
class AvailableProductController(
    private val availableProductService: AvailableProductService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAllAvailableProducts() = availableProductService.getAllAvailableProducts()
}