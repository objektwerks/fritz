group = "objektwerks"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
}

repositories {
    mavenCentral()
}

val kotlinVersion = "1.9.22"
val fritz2Version = "1.0-RC15"
val ktorVersion = "2.3.8"
val exposedVersion = "0.47.0"
val hopliteVersion = "2.7.5"

kotlin {
    jvm()
    js(IR) {
        browser()
    }.binaries.executable()
    sourceSets {
        commonMain.dependencies {
            implementation("dev.fritz2:core:$fritz2Version")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        }
        commonTest.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        }
        jvmMain.dependencies {
            implementation("io.ktor:ktor-server-core:$ktorVersion")
            implementation("io.ktor:ktor-server-netty:$ktorVersion")

            implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

            implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
            implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")

            implementation("com.h2database:h2:2.2.224")

            implementation("ch.qos.logback:logback-classic:1.4.14")
        }
        jvmTest.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        }
        jsMain.dependencies {
        }
        jsTest.dependencies {
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "dev.fritz2:lenses-annotation-processor:$fritz2Version")
}
kotlin.sourceSets.commonMain { kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin") }
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") dependsOn("kspCommonMainKotlinMetadata")
}