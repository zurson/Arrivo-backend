package com.arrivo.utilities.sql_data_loader

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.nio.file.Files

@Component
class SqlDataLoader(
    private val jdbcTemplate: JdbcTemplate,
    @Value("classpath:available_products.sql") private val sqlFile: Resource
) {

    fun loadData() {
        try {
            val sqlContent = Files.readString(sqlFile.file.toPath())
            jdbcTemplate.execute(sqlContent)

            println("Data loaded successfully from SQL file.")
        } catch (e: Exception) {
            println("Error loading data from SQL file: ${e.message}")
        }
    }
}