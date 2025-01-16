package com.arrivo.utilities.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAPIConfiguration {

    @Bean
    fun defineOpenApi(): OpenAPI {
        val server = Server()
        server.url = "http://localhost:8080"
        server.description = "Development"

        val myContact = Contact()
        myContact.name = "Jane Doe"
        myContact.email = "your.email@gmail.com"

        val information: Info = Info()
            .title("Testing API")
            .version("1.0")
            .description("This API exposes some endpoints.")
            .contact(myContact)
        return OpenAPI().info(information).servers(listOf(server))
    }

}