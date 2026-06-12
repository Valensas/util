package com.valensas.util.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.rest")
data class PageableConfig(
    val defaultPageSize: Int?,
    val maxPageSize: Int?
)
