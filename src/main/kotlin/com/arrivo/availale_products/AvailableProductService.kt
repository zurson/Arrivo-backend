package com.arrivo.availale_products

import com.arrivo.firebase.FirebaseService
import org.springframework.stereotype.Service

@Service
class AvailableProductService(
    private val availableProductRepository: AvailableProductRepository,
    private val firebaseService: FirebaseService
) {
    fun getAllAvailableProducts(): List<AvailableProductDTO> {
        val company = firebaseService.getUserCompany()
        return availableProductRepository.findAllAvailableProductsInCompany(company.id)
            .map { AvailableProductDTO(it.name) }
    }

    fun findById(id: Long): AvailableProduct =
        availableProductRepository.findById(id).orElseThrow {
            IllegalArgumentException("AvailableProduct with id $id not found")
        }
}