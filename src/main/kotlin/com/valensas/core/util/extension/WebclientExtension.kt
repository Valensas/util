// ktlint-disable filename
package com.valensas.core.util.extension

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

suspend fun WebClient.ResponseSpec.awaitEmptyBody() {
    bodyToMono<Void>().awaitFirstOrNull()
}
