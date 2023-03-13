package com.valensas.util

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

fun measure(meterRegistry: MeterRegistry, name: String, tags: Map<String, String>, block: () -> Unit) {
    var exception: Throwable? = null
    val nanoDuration = measureNanoTime {
        try {
            block()
        } catch (e: Throwable) {
            exception = e
        }
    }

    val extraTags = if (exception == null) {
        mapOf("exception" to "none")
    } else {
        mapOf("exception" to exception!!.javaClass.canonicalName)
    }

    meterRegistry.timer(name, (tags + extraTags).map { Tag.of(it.key, it.value) }).record(nanoDuration, TimeUnit.NANOSECONDS)
    if (exception != null) throw exception!!
}

fun runScheduledTask(
    meterRegistry: MeterRegistry,
    taskName: String,
    metricName: String = "cron_job",
    tags: Map<String, String> = emptyMap(),
    block: suspend () -> Unit
) = measure(meterRegistry, metricName, tags + mapOf("name" to taskName)) {
    runBlocking { block() }
}
