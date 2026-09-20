# NuFli Backend Development Guide

## Prerequisites
- Java 21 (Temurin JDK recommended)
- Docker & Docker Compose
- Git

## Running Locally

1. **Start PostgreSQL Container:**
   ```bash
   docker compose up postgres -d
   ```

2. **Run Spring Boot Application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Access Swagger UI:**
   Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Running Tests

Run the complete test suite:
```bash
./gradlew test
```

## Creating New Migrations
Add sequentially named SQL files in `src/main/resources/db/migration/`:
```text
V5__your_feature_name.sql
```
Never modify existing migration scripts that have already run on shared environments.
