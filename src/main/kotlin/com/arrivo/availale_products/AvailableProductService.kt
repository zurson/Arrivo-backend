package com.arrivo.availale_products

import org.springframework.stereotype.Service

@Service
class AvailableProductService(
    private val availableProductRepository: AvailableProductRepository
) {
    fun getAllAvailableProducts(): List<AvailableProductDTO> {
        return availableProductRepository.findAll().map { AvailableProductDTO(it.name) }
    }

    fun findById(id: Long): AvailableProduct =
        availableProductRepository.findById(id).orElseThrow {
            IllegalArgumentException("AvailableProduct with id $id not found")
        }
}