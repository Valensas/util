package com.valensas.util.autoconfigure

import org.slf4j.LoggerFactory
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.util.TypeScanner

@Configuration
@ImportRuntimeHints(RuntimeHintsAutoConfiguration.ClientHintsRegistrar::class)
class RuntimeHintsAutoConfiguration {
    class ClientHintsRegistrar : RuntimeHintsRegistrar {
        private val logger = LoggerFactory.getLogger(javaClass)
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            val packageNames = System.getProperty("com.valensas.util.reflect-packages") ?: return
            logger.info("Setting reflection hints classes in packages: {}", packageNames)

            val packages = packageNames.split(",", " ", "\n").filter { it.isNotBlank() }

            TypeScanner
                .typeScanner(classLoader!!)
                .scanPackages(packages)
                .forEach {
                    hints.reflection().registerType(it, MemberCategory.INVOKE_PUBLIC_METHODS)
                }
        }
    }
}
