# NuFli Backend

> **Shared Core Backend for Nutrity and Flitness**  
> *Simplify your own life.*

NuFli Backend is the centralized identity, authentication, nutrition, and fitness service providing the core API for both the **Nutrity** (Nutrition) and **Flitness** (Fitness) mobile applications.

---

## Key Features

- **Unified Account & Auth:** JWT Access Token + Stateful Rotating Refresh Token authentication model.
- **Isolated Domain Boundaries:** Complete separation between Nutrition and Fitness domain models with zero coupling.
- **Deterministic Nutritional Engine:** Exact Mifflin-St Jeor BMR, TDEE, and macronutrient rollup calculations.
- **Strength & Endurance Fitness Engine:** Dynamic volume tracking, 1RM estimation (Epley), and MET cardio energy burn estimations.
- **Cross-Domain Aggregation:** Real-time daily energy balance views without toxic "eat-back-your-burn" bias.
- **OpenAPI 3 / Swagger Documentation:** Interactive documentation at `/swagger-ui.html`.
- **Database Migrations:** Managed schema evolutions using Flyway.

---

## Technology Stack

- **Language & Runtime:** Java 21 (Temurin LTS)
- **Framework:** Spring Boot 3.4.3
- **Data Persistence:** Spring Data JPA / Hibernate
- **Database:** PostgreSQL 16
- **Migrations:** Flyway
- **Security:** Spring Security 6 + JJWT (0.12.6)
- **Documentation:** SpringDoc OpenAPI 2.8.5
- **Build Tool:** Gradle 9.2.1

---

## Quick Start (Docker)

```bash
docker compose up --build -d
```

The server will be available at `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) - Hexagonal architecture, domain boundaries, and design principles.
- [API.md](API.md) - Endpoint catalog, request/response models, and status codes.
- [DATABASE.md](DATABASE.md) - PostgreSQL schema, indexes, constraints, and Flyway migrations.
- [DEVELOPMENT.md](DEVELOPMENT.md) - Local development setup, testing, and debugging guidelines.
