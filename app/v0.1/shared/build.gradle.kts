plugins {
    val kotlin = "2.2.10"
    kotlin("multiplatform") version kotlin
    kotlin("plugin.serialization") version kotlin
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js().nodejs()
    if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinx = "org.jetbrains.kotlinx:kotlinx"
                implementation("$kotlinx-coroutines-core:1.10.2")
                implementation("$kotlinx-datetime:0.7.1")
                implementation("$kotlinx-serialization-json:1.9.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks.register<Copy>("copyEnglishBundlesToBase") {
    group = "build"
    description = "Copies English Resource Bundles to base bundles and that way system default locale is overridden."
    from("src/jvmMain/resources") { include("**/*_en.properties") }
    into("src/jvmMain/resources")
    rename { it.replace("_en.properties", ".properties") }
}

tasks.register<Delete>("cleanBaseBundles") {
    group = "build"
    description = "Removes base ResourceBundle files, because they are created by English on build."
    delete(fileTree("src/jvmMain/resources") {
        include("*.properties")
        exclude("*_*.properties")
    })
}

tasks.named("jvmProcessResources") { dependsOn("copyEnglishBundlesToBase") }

tasks.named("clean") { dependsOn("cleanBaseBundles") }