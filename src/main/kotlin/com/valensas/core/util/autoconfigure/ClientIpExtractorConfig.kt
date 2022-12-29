package com.valensas.core.util.autoconfigure

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

@Component
class ClientIpExtractorConfig(
    @Value("\${management.endpoints.web.base-path:/actuator}")
    private val managementBasePath: String
) : WebFilter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(serverWebExchange: ServerWebExchange, webFilterChain: WebFilterChain): Mono<Void> {
        if (serverWebExchange.request.path.toString().startsWith(managementBasePath)) return webFilterChain.filter(serverWebExchange)

        val clientIp = serverWebExchange.request.headers.getFirst("x-client-ip")
        if (clientIp == null) logger.warn("Failed to fetch client ip from header x-client-ip")
        return webFilterChain.filter(serverWebExchange)
            .contextWrite { context: Context ->
                clientIp?.let { context.put("ClientIp", clientIp) } ?: context
            }
    }
}
