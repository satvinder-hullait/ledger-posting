import com.diffplug.gradle.spotless.JavaExtension

val googleJavaFormatVersion: String by project
val springDocVersion: String by project
val springDataCommonsVersion: String by project

plugins {
	java
	id("base")
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.diffplug.spotless") version "6.25.0"
}

group = "com.zing.ledger"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	google()
}

//	Dependencies without versions are managed from: https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.data:spring-data-redis")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springDocVersion}")
	implementation("org.springframework.data:spring-data-commons:${springDataCommonsVersion}")
	implementation("redis.clients:jedis")


	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("io.rest-assured:rest-assured")
	testImplementation("org.assertj:assertj-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.assertj:assertj-core")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:testcontainers")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		googleJavaFormat(googleJavaFormatVersion)
			.aosp()
			.reflowLongStrings()
			.formatJavadoc(false)
			.groupArtifact("com.google.googlejavaformat:google-java-format")
	}
}