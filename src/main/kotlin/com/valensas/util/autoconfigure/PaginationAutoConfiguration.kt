package com.valensas.util.autoconfigure

import com.valensas.util.config.PageableConfig
import com.valensas.util.pagination.Pageable
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@RegisterReflectionForBinding(classes = [Pageable::class])
@EnableConfigurationProperties(PageableConfig::class)
class PaginationAutoConfiguration(
    pageableConfig: PageableConfig
) {
    init {
        pageableConfig.defaultPageSize?.let {
            Pageable.defaultPageSize = it
        }
        pageableConfig.maxPageSize?.let {
            Pageable.maxPageSize = it
        }
    }
}
