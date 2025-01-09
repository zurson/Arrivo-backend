package com.arrivo.utilities.sql_data_loader

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataLoaderRunner(
    private val sqlDataLoader: SqlDataLoader
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        sqlDataLoader.loadData()
    }
}