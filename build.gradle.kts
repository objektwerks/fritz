import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

group = "objektwerks"
version = "0.6-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("application")
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

val kotlinVersion = "2.1.0"
val kotlinxSerializationVersion = "1.7.3"
val fritz2Version = "1.0-RC19.4"
val ktorVersion = "3.0.1"
val exposedVersion = "0.56.0"
val hopliteVersion = "2.9.0"

kotlin {
    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set("objektwerks.Server")
        }
    }
    js(IR) {
        browser()
    }.binaries.executable()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.fritz2:core:$fritz2Version")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-server-cors:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

                implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
                implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")
                implementation("org.yaml:snakeyaml:2.2")

                implementation("com.sksamuel.aedile:aedile-core:2.0.0")

                implementation("org.postgresql:postgresql:42.7.4")
                implementation("com.zaxxer:HikariCP:5.1.0")

                implementation("org.jodd:jodd-mail:7.1.0")

                implementation("ch.qos.logback:logback-classic:1.5.9")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
                implementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}

dependencies.kspCommonMainMetadata("dev.fritz2:lenses-annotation-processor:$fritz2Version")
kotlin.sourceSets.commonMain {
    tasks.withType<KspTaskMetadata> {
        kotlin.srcDir(destinationDirectory)
    }
}
