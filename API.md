# NuFli Backend API Specification

All endpoints are versioned with the `/api/v1` prefix.

## 1. Authentication & Users

### `POST /api/v1/auth/register`
* **Body:** `{ "email": "user@example.com", "password": "Password123!", "firstName": "John", "lastName": "Doe" }`
* **Response (201 Created):** `{ "accessToken": "...", "refreshToken": "...", "expiresInSeconds": 900, "user": {...} }`

### `POST /api/v1/auth/login`
* **Body:** `{ "email": "user@example.com", "password": "Password123!" }`
* **Response (200 OK):** `{ "accessToken": "...", "refreshToken": "...", "expiresInSeconds": 900, "user": {...} }`

### `POST /api/v1/auth/refresh`
* **Body:** `{ "refreshToken": "..." }`
* **Response (200 OK):** New token pair with rotated refresh token.

### `GET /api/v1/users/me`
* **Header:** `Authorization: Bearer <accessToken>`
* **Response (200 OK):** User account and demographic profile.

### `PUT /api/v1/users/me/profile`
* **Body:** `{ "firstName": "John", "lastName": "Doe", "birthDate": "1995-06-15", "biologicalSex": "MALE", "heightCm": 182.0, "activityLevel": "MODERATELY_ACTIVE" }`

---

## 2. Nutrition Domain

### `GET /api/v1/nutrition/foods?query=chicken&page=0&size=20`
Search food library (returns verified system foods and private user foods).

### `POST /api/v1/nutrition/foods`
Create a custom food item with nutritional values per 100g.

### `POST /api/v1/nutrition/meals`
Log a meal with food items and gram quantities:
```json
{
  "mealType": "LUNCH",
  "loggedDate": "2026-09-20",
  "items": [
    { "foodId": "11111111-1111-1111-1111-111111111101", "quantityGrams": 200 },
    { "foodId": "11111111-1111-1111-1111-111111111102", "quantityGrams": 150 }
  ]
}
```

### `GET /api/v1/nutrition/meals?date=YYYY-MM-DD`
Retrieve all meals logged for a calendar date.

### `POST /api/v1/nutrition/recipes`
Create a recipe with ingredient food references.

### `POST /api/v1/nutrition/weight`
Log daily body weight in kg.

### `POST /api/v1/nutrition/goals`
Set custom target calories or let backend auto-calculate targets from profile BMR and TDEE.

### `GET /api/v1/nutrition/summary?date=YYYY-MM-DD`
Retrieve consumed vs target calories and macros.

---

## 3. Fitness Domain

### `GET /api/v1/fitness/exercises?muscle=CHEST&category=BARBELL`
Search verified and custom exercises.

### `POST /api/v1/fitness/routines`
Create reusable workout routine templates with exercises, target sets, and target reps.

### `POST /api/v1/fitness/workouts`
Start or log a workout session with exercises, sets, weights, reps, and RPE.

### `POST /api/v1/fitness/activities`
Log cardio or endurance activities (Running, Walking, Cycling, HIIT) with distance and calories burned.

### `GET /api/v1/fitness/summary?date=YYYY-MM-DD`
Get daily fitness totals (completed workouts, volume, cardio duration, estimated calories burned).

---

## 4. Ecosystem Cross-Domain

### `GET /api/v1/summary/daily?date=YYYY-MM-DD`
Aggregated view combining consumed nutrition and fitness expenditure.
