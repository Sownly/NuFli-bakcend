CREATE TABLE foods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    serving_unit VARCHAR(50) NOT NULL DEFAULT 'g',
    serving_size_grams NUMERIC(8, 2) NOT NULL DEFAULT 100.0,
    calories_per_100g NUMERIC(8, 2) NOT NULL,
    protein_per_100g NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    carbs_per_100g NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    fat_per_100g NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    fiber_per_100g NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE meals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    meal_type VARCHAR(50) NOT NULL,
    logged_date DATE NOT NULL,
    logged_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE meal_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meal_id UUID NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
    food_id UUID NOT NULL REFERENCES foods(id) ON DELETE RESTRICT,
    quantity_grams NUMERIC(8, 2) NOT NULL,
    calculated_calories NUMERIC(8, 2) NOT NULL,
    calculated_protein NUMERIC(8, 2) NOT NULL,
    calculated_carbs NUMERIC(8, 2) NOT NULL,
    calculated_fat NUMERIC(8, 2) NOT NULL
);

CREATE TABLE recipes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    servings INTEGER NOT NULL DEFAULT 1,
    preparation_time_minutes INTEGER NOT NULL DEFAULT 0,
    instructions TEXT,
    total_calories NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    total_protein NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    total_carbs NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    total_fat NUMERIC(8, 2) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recipe_ingredients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id UUID NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    food_id UUID NOT NULL REFERENCES foods(id) ON DELETE RESTRICT,
    quantity_grams NUMERIC(8, 2) NOT NULL
);

CREATE TABLE weight_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recorded_date DATE NOT NULL,
    weight_kg NUMERIC(5, 2) NOT NULL,
    notes TEXT,
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_weight_user_date UNIQUE (user_id, recorded_date)
);

CREATE TABLE nutrition_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    effective_date DATE NOT NULL,
    target_calories NUMERIC(8, 2) NOT NULL,
    target_protein_grams NUMERIC(8, 2) NOT NULL,
    target_carbs_grams NUMERIC(8, 2) NOT NULL,
    target_fat_grams NUMERIC(8, 2) NOT NULL,
    goal_type VARCHAR(50) NOT NULL DEFAULT 'MAINTAIN',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_foods_user ON foods(user_id);
CREATE INDEX idx_foods_name ON foods(name);
CREATE INDEX idx_meals_user_date ON meals(user_id, logged_date);
CREATE INDEX idx_meal_items_meal ON meal_items(meal_id);
CREATE INDEX idx_recipes_user ON recipes(user_id);
CREATE INDEX idx_weight_user_date ON weight_entries(user_id, recorded_date DESC);
CREATE INDEX idx_nutrition_goals_user ON nutrition_goals(user_id, is_active);
