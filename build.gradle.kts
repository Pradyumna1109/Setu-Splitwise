plugins {
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("com.diffplug.spotless") version "6.19.0"
	application
	id("io.ebean") version "13.15.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	mavenLocal()
}

extra["testcontainersVersion"] = "1.17.6"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
		exclude(group = "org.hibernate", module = "hibernate-core")
	}
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")
	implementation("org.springframework.boot:spring-boot-starter-security")

	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-core:9.8.1")
	implementation("com.zaxxer:HikariCP:5.0.1")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("com.cronutils:cron-utils:9.2.0")
	implementation("io.ebean:ebean:13.15.0")
	implementation("com.google.guava:guava:32.0.1-jre")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("commons-io:commons-io:2.9.0")
	implementation("javax.inject:javax.inject:1")
	implementation("org.codehaus.janino:janino:3.1.9")
	implementation("net.logstash.logback:logstash-logback-encoder:7.3")
	implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
	implementation("org.apache.velocity:velocity:1.7")
	implementation("org.apache.velocity:velocity-tools:2.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.glassfish:jakarta.el:3.0.4")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
		// TODO use version from common instead of explicit one
		mavenBom("com.fasterxml.jackson:jackson-bom:2.17.2")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		removeUnusedImports()
		googleJavaFormat()
		target("src/**/*.java")
		importOrder("\\#","")
	}
}
