import java.util.UUID

plugins {
    id("java")
    id("io.qameta.allure") version "3.0.1"
}

group = "ua.com.vladyslav.spribe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val allureVersion = "2.29.0"

dependencies {
    implementation("org.testng:testng:7.11.0")
    implementation("io.rest-assured:rest-assured:5.5.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("io.qameta.allure:allure-testng:$allureVersion")
    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
}

tasks.test {
    doFirst {
        val runId = "run-" + UUID.randomUUID().toString().substring(0, 8)
        systemProperty("log.session.id", runId)
        println("=== test: setting log.session.id = $runId ===")
    }
    val allureResultsDir = layout.buildDirectory.dir("allure-results")

    useTestNG {
        suites("src/test/resources/testng.xml")
    }
    systemProperty("allure.results.directory", allureResultsDir.get().asFile.absolutePath)
    systemProperties(System.getProperties().map { it.key.toString() to it.value }.toMap())
}

allure {
    version.set(allureVersion)
    adapter {
        frameworks {
            testng {
                adapterVersion.set(allureVersion)
            }
        }
    }
    report {
        version.set(allureVersion)
    }
}