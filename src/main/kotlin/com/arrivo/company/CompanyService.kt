package com.arrivo.company

import com.arrivo.exceptions.IdNotFoundException
import org.springframework.stereotype.Service

@Service
class CompanyService(private val repository: CompanyRepository) {

    fun findById(id: Long): Company {
        return repository.findById(id).orElseThrow {
            IdNotFoundException("Company with ID $id not found")
        }
    }


    fun save(company: Company): Company {
        return repository.save(company)
    }


    fun toDto(company: Company): CompanyDTO {
        return CompanyDTO(
            id = company.id,
            location = company.location,
            name = company.name,
            phoneNumber = company.phoneNumber
        )
    }

}