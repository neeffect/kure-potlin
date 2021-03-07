plugins {
    `maven-publish`
    kotlin("jvm") version "1.4.31"
}
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    // When creating a sample extension, change this dependency to the detekt-api version you build against
    // e.g. io.gitlab.arturbosch.detekt:detekt-api:1.x.x
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.15.0")
    // When creating a sample extension, change this dependency to the detekt-test version you build against
    // e.g. io.gitlab.arturbosch.detekt:detekt-test:1.x.x
    testImplementation("io.gitlab.arturbosch.detekt:detekt-api:1.15.0")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.15.0")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.15")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "pl.setblack"
            artifactId = "kure-potlin"
            version = "0.1.3-SNAPSHOT"

            from(components["java"])
        }
    }
}
