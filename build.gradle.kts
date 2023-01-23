import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jmailen.kotlinter") version "3.13.0"
    id("maven-publish")
    id("java-library")
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.valensas"
version = "2.2.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    if (project.hasProperty("AWS_REPO_URL")) {
        maven {
            this.url = uri(project.property("AWS_REPO_URL").toString())
            credentials(AwsCredentials::class) {
                this.accessKey = project.property("AWS_REPO_USER_ACCESS_KEY").toString()
                this.secretKey = project.property("AWS_REPO_USER_SECRET_KEY").toString()
            }
        }
    }
}


dependencies {
    // Kotlin reflection support
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("org.springframework.data:spring-data-commons")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            this.url = uri(project.property("AWS_REPO_URL").toString())
            credentials(AwsCredentials::class) {
                this.accessKey = project.property("AWS_REPO_ADMIN_ACCESS_KEY").toString()
                this.secretKey = project.property("AWS_REPO_ADMIN_SECRET_KEY").toString()
            }
        }
    }

    publications {
        create<MavenPublication>("artifact") {
            from(components["java"])
        }
    }
}
