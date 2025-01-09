package com.arrivo.task.products

import org.springframework.stereotype.Service

@Service
class ProductService(private val repository: ProductRepository) {
    fun findAll(): List<Product> {
        return repository.findAll()
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}
