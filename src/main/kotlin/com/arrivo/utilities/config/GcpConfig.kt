package com.arrivo.utilities.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "gcp")
class GcpConfig {
    lateinit var projectId: String
}