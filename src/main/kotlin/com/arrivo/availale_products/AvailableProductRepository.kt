package com.arrivo.availale_products

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AvailableProductRepository : JpaRepository<AvailableProduct, Long> {

    @Query("SELECT a FROM AvailableProduct a WHERE a.company.id = :companyId")
    fun findAllAvailableProductsInCompany(companyId: Long): List<AvailableProduct>

}