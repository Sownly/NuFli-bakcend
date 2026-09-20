# NuFli Backend Database Specification

## Database Engine: PostgreSQL 16+

The database schema is managed strictly through Flyway migrations.

## Migration Files

1. `V1__create_identity_tables.sql`:
   - `users`: User identity, email, password hash, status.
   - `profiles`: Demographics (height, biological sex, birth date, activity level).
   - `refresh_tokens`: Token hashes, expiration, revocation tracking.
2. `V2__create_nutrition_tables.sql`:
   - `foods`: Nutritional breakdown per 100g.
   - `meals` & `meal_items`: Daily food consumption records with immutable calculated macro snapshots.
   - `recipes` & `recipe_ingredients`: User recipes and component ingredients.
   - `weight_entries`: Daily body weight logs with unique constraint `(user_id, recorded_date)`.
   - `nutrition_goals`: Target calories and macro splits.
3. `V3__create_fitness_tables.sql`:
   - `exercises`: Strength and cardio movements catalog.
   - `routines` & `routine_exercises`: Reusable workout templates.
   - `workouts`, `workout_exercises`, `exercise_sets`: Live and completed workout execution logs.
   - `activities`: Cardio / endurance logs.
4. `V4__seed_verified_data.sql`:
   - 10 verified core foods (Chicken Breast, White Rice, Eggs, Rolled Oats, Banana, Olive Oil, Whey Protein, etc.).
   - 10 verified foundational exercises (Bench Press, Squat, Deadlift, Overhead Press, Pull-Up, Running, etc.).

## Indexing Strategy
- Composite indexes on `(user_id, logged_date)` for O(1) daily logging queries.
- Reverse chronological index on `(user_id, recorded_date DESC)` for weight and workout history.
- Partial index on `(user_id, is_active)` for active nutritional targets.
