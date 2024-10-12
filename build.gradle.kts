plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

buildscript {
    val agp_version by extra("3.2.0")
    val agp_version1 by extra("8.2.1")
}