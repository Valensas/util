package com.valensas.util.autoconfigure

import org.slf4j.LoggerFactory
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.data.util.TypeScanner
import kotlin.reflect.jvm.internal.ReflectionFactoryImpl

@Configuration
@ImportRuntimeHints(
    RuntimeHintsAutoConfiguration.ReflectionHintsRegistrar::class,
    RuntimeHintsAutoConfiguration.KotlinTypesRegistrar::class
)
class RuntimeHintsAutoConfiguration {

    /**
     * Registers runtime hints for all classes in packages defined in the
     * `com.valensas.util.reflect-packages` system property.
     */
    class ReflectionHintsRegistrar : RuntimeHintsRegistrar {
        private val logger = LoggerFactory.getLogger(javaClass)
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            val packageNames = System.getProperty("com.valensas.util.reflect-packages") ?: return
            val packages = packageNames.split(",", " ", "\n").filter { it.isNotBlank() }

            logger.info("Setting reflection hints for classes in packages: {}", packages.joinToString(", "))

            TypeScanner
                .typeScanner(classLoader!!)
                .scanPackages(packages)
                .forEach {
                    hints.reflection().registerType(it, MemberCategory.INVOKE_PUBLIC_METHODS)
                }
        }
    }

    /**
     * Register runtime hints for Kotlin internal types.
     * See https://dev.to/jonastm/current-state-of-spring-boot-27-native-with-kotlin-graalvm-3dda
     */
    class KotlinTypesRegistrar : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            val kotlinInternalTypes = listOf(
                ReflectionFactoryImpl::class.java,
                KotlinVersion::class.java,
                Array<KotlinVersion>::class.java,
                KotlinVersion.Companion::class.java,
                Array<KotlinVersion.Companion>::class.java,
                // This class package-internal and cannot be imported here.
                // jdk8-named classes are used even with Java 17.
                classLoader!!.loadClass("kotlin.internal.jdk8.JDK8PlatformImplementations")
            )

            kotlinInternalTypes.forEach {
                hints.reflection().registerType(
                    it,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.DECLARED_FIELDS,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
                )
            }
        }
    }
}
