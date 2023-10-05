plugins {
    java
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("io.vertx:vertx-core:4.0.3")
    implementation("ch.qos.logback:logback-classic:1.2.9")
}

tasks.create<JavaExec>("run") {
    mainClass = project.properties.getOrDefault("mainClass", "com.andy.vertx_learning.p2_verticles.HelloVerticle") as String
    classpath = sourceSets["main"].runtimeClasspath
    systemProperties["vertx.logger-delegate-factory-class-name"] = "io.vertx.core.logging.SLF4JLogDelegateFactory"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.wrapper {
    gradleVersion = "8.3"
}