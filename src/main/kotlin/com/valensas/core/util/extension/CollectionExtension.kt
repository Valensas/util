// ktlint-disable filename
package com.valensas.core.util.extension

import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

suspend fun <T, U> Collection<T>.parallelForEach(mapper: suspend (T) -> U) {
    if (this.isEmpty()) return
    Flux.fromIterable(this).parallel().flatMap { mono { mapper(it) } }.awaitLast()
}

suspend fun <T, U> Collection<T>.parallelMap(mapper: suspend (T) -> U): List<U> {
    if (this.isEmpty()) return emptyList()
    return Flux.fromIterable(this).flatMap { mono { mapper(it) } }.collectList().awaitSingle()
}
