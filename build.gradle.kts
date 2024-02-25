import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

group = "objektwerks"
version = "0.1-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "2.0.0-Beta4"
    kotlin("plugin.serialization") version "2.0.0-Beta4"
    id("application")
    id("com.adarshr.test-logger") version "4.0.0"
    id("com.google.devtools.ksp") version "2.0.0-Beta4-1.0.17"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val kotlinVersion = "2.0.0-Beta4"
val kotlinxSerializationVersion = "1.6.3"
val fritz2Version = "1.0-RC16"
val ktorVersion = "2.3.8"
val exposedVersion = "0.47.0"
val hopliteVersion = "2.7.5"

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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

                implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
                implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")
                implementation("org.yaml:snakeyaml:2.2")

                implementation("com.h2database:h2:2.2.224")

                implementation("ch.qos.logback:logback-classic:1.5.0")
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