
@file:Suppress("ktlint:standard:filename")

package com.valensas.util.extension

import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

suspend fun <T> Collection<T>.parallelForEach(mapper: suspend (T) -> Unit) {
    if (this.isEmpty()) return
    Flux.fromIterable(this).parallelForEach(mapper)
}

suspend fun <T, U> Collection<T>.parallelMap(mapper: suspend (T) -> U): List<U> {
    if (this.isEmpty()) return emptyList()
    return Flux.fromIterable(this).flatMap { mono { mapper(it) } }.collectList().awaitSingle()
}

suspend fun <T> Flux<T>.parallelForEach(function: suspend (T) -> Unit) {
    this.flatMap { mono { function(it) } }.switchIfEmpty(Mono.just(Unit)).awaitLast()
}
