import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    `maven-publish`
    kotlin("jvm") version "1.4.31"
    signing
    id("org.jetbrains.dokka") version "0.10.1"
}
repositories {
    mavenCentral()
    jcenter()
}

val componentVersion = "0.2.1"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.15.0")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.4.31")
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
            version = componentVersion

            from(components["java"])
        }
    }
}

val ossrhUsername: String? by project
val ossrhPassword: String? by project
val signingKey: String? by project
val signingPassword: String? by project

val isRelease = !componentVersion.endsWith("-SNAPSHOT")


val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications

signing {

    if (signingKey != null && signingPassword != null) {
        @Suppress("UnstableApiUsage")
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    useGpgCmd()
    sign(publications)
}

val dokka = tasks.named("dokka")

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokka)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.apply {
    jvmTarget = "11"
    javaParameters = true
    allWarningsAsErrors = true
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            name = "deploy"
            println("isRelease: $isRelease")
            url = if (isRelease) releasesRepoUrl else snapshotsRepoUrl
            credentials {
                username = System.getenv("OSSRH_USERNAME") ?: ossrhUsername
                password = System.getenv("OSSRH_PASSWORD") ?: ossrhPassword
            }
        }
    }

    publications.forEach {
        println("publication $it")
    }

    publications.withType<MavenPublication>().forEach {
        it.apply {
            if (isRelease) {
                artifact(tasks["dokkaJar"])
            }
            artifact(tasks["kotlinSourcesJar"])

            pom {
                name.set("kure-potlin")
                description.set("kure-potlin")
                url.set("https://github.com/neeffect/kure-potlin")

                scm {
                    connection.set("scm:git:https://github.com/neeffect/kure-potlin.git")
                    developerConnection.set("scm:git:http://github.com/jarekratajski/")
                    url.set("https://github.com/neeffect/kure-potlin")
                }

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("jarekratajski")
                        name.set("Jarek Ratajski")
                        email.set("jratajski@gmail.com")
                    }
                }
            }
        }
    }
}

