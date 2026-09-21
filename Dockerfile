# Stage 1: Build application with Gradle
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace

COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY gradle gradle/
RUN ./gradlew dependencies --no-daemon || true

COPY src src/
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Robust glibc runtime image (fixes musl TLS socket broken pipe)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN useradd -m -u 1000 nufli
USER nufli:nufli

COPY --from=builder /workspace/build/libs/nufli-backend-1.0.0.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2,TLSv1.3", "-jar", "app.jar"]
