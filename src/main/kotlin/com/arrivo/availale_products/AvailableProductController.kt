package com.arrivo.availale_products

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/available-products")
class AvailableProductController(
    private val availableProductService: AvailableProductService
) {
    @GetMapping
    fun getAllAvailableProducts() = availableProductService.getAllAvailableProducts()
}