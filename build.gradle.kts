plugins {
    kotlin("jvm") version "1.7.0"
    antlr
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

    dependencies {
        implementation(kotlin("stdlib"))
        antlr("org.antlr:antlr4:4.5")
        implementation("org.antlr:antlr4-runtime:4.7.1")
        implementation("com.tictactec:ta-lib:0.4.0")
    }
    tasks.generateGrammarSource {
        maxHeapSize = "64m"
        arguments = arguments + listOf("-visitor", "-long-messages")
    }
