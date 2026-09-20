# NuFli Backend Architecture

## Architectural Style: Hexagonal / Clean Architecture

The backend follows Clean Architecture principles with package-by-feature domain modularity:

```text
com.sownly.nufli
├── common/                  # Cross-cutting exceptions and RFC 7807 responses
├── domain/                  # Pure enterprise business rules, entities, and calculators
│   ├── identity/            # User, Profile, RefreshToken
│   ├── nutrition/           # Food, Meal, Recipe, Weight, Goals, NutritionCalculator
│   ├── fitness/             # Exercise, Routine, Workout, Activity, FitnessCalculator
│   └── summary/             # Aggregated models
├── application/             # Application orchestration / Use cases
│   ├── identity/            # AuthService, UserService
│   ├── nutrition/           # NutritionService
│   ├── fitness/             # FitnessService
│   └── summary/             # SummaryService
├── infrastructure/          # Framework adapters
│   ├── config/              # OpenAPI, Web config
│   └── security/            # JWT provider, filters, Spring Security configuration
└── presentation/web/        # REST API endpoints
    ├── identity/            # AuthController, UserController
    ├── nutrition/           # FoodController, MealController, RecipeController, etc.
    ├── fitness/             # ExerciseController, WorkoutController, RoutineController, etc.
    ├── summary/             # SummaryController
    └── error/               # GlobalExceptionHandler
```

## Domain Boundaries & Decoupling

1. **Zero Foreign Keys between Nutrition and Fitness:**
   The `foods`, `meals`, and `recipes` tables have zero links to `exercises` or `workouts`.
   Both domains only link to `users(id)`.
2. **Cross-Domain Aggregation:**
   The `SummaryService` interacts with `NutritionService` and `FitnessService` as read-only queries, producing an informational summary for daily energy balance.
3. **Deterministic Math Invariants:**
   Calculations (BMR, TDEE, Epley 1RM, MET burn) live exclusively in pure, stateless domain calculators without external dependencies.
