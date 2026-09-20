-- Seed verified nutrition foods (user_id IS NULL, is_verified = TRUE)
INSERT INTO foods (id, user_id, name, brand, serving_unit, serving_size_grams, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g, fiber_per_100g, is_verified) VALUES
('11111111-1111-1111-1111-111111111101', NULL, 'Chicken Breast (Cooked, Skinless)', 'Generic', 'g', 100.0, 165.0, 31.0, 0.0, 3.6, 0.0, TRUE),
('11111111-1111-1111-1111-111111111102', NULL, 'White Rice (Cooked)', 'Generic', 'g', 100.0, 130.0, 2.7, 28.2, 0.3, 0.4, TRUE),
('11111111-1111-1111-1111-111111111103', NULL, 'Whole Egg (Large, Boiled)', 'Generic', 'unit', 50.0, 155.0, 13.0, 1.1, 11.0, 0.0, TRUE),
('11111111-1111-1111-1111-111111111104', NULL, 'Rolled Oats (Dry)', 'Generic', 'g', 100.0, 389.0, 16.9, 66.3, 6.9, 10.6, TRUE),
('11111111-1111-1111-1111-111111111105', NULL, 'Banana (Raw)', 'Generic', 'g', 100.0, 89.0, 1.1, 22.8, 0.3, 2.6, TRUE),
('11111111-1111-1111-1111-111111111106', NULL, 'Olive Oil (Extra Virgin)', 'Generic', 'ml', 15.0, 884.0, 0.0, 0.0, 100.0, 0.0, TRUE),
('11111111-1111-1111-1111-111111111107', NULL, 'Whey Protein Isolate (Vanilla)', 'Optimum Nutrition', 'g', 30.0, 390.0, 80.0, 6.0, 3.0, 0.0, TRUE),
('11111111-1111-1111-1111-111111111108', NULL, 'Salmon Fillet (Baked)', 'Generic', 'g', 100.0, 208.0, 22.0, 0.0, 13.0, 0.0, TRUE),
('11111111-1111-1111-1111-111111111109', NULL, 'Greek Yogurt (0% Fat)', 'Generic', 'g', 100.0, 59.0, 10.0, 3.6, 0.4, 0.0, TRUE),
('11111111-1111-1111-1111-111111111110', NULL, 'Almonds (Raw)', 'Generic', 'g', 100.0, 579.0, 21.2, 21.6, 49.9, 12.5, TRUE);

-- Seed verified exercises (user_id IS NULL, is_verified = TRUE)
INSERT INTO exercises (id, user_id, name, primary_muscle_group, secondary_muscle_groups, category, instructions, is_verified) VALUES
('22222222-2222-2222-2222-222222222201', NULL, 'Barbell Bench Press', 'CHEST', 'Triceps, Front Deltoids', 'BARBELL', 'Lie flat on bench, retract scapula, lower bar to mid-chest, press upward explosively.', TRUE),
('22222222-2222-2222-2222-222222222202', NULL, 'Barbell Back Squat', 'LEGS', 'Glutes, Lower Back, Core', 'BARBELL', 'Bar rested on upper traps, squat down below parallel keeping knees tracking toes, drive through mid-foot.', TRUE),
('22222222-2222-2222-2222-222222222203', NULL, 'Conventional Deadlift', 'BACK', 'Hamstrings, Glutes, Forearms', 'BARBELL', 'Feet hip-width, grip bar outside legs, brace core, push floor away maintaining neutral spine.', TRUE),
('22222222-2222-2222-2222-222222222204', NULL, 'Overhead Shoulder Press', 'SHOULDERS', 'Triceps, Upper Chest', 'BARBELL', 'Stand upright, press barbell overhead locking elbows at top while maintaining tight glutes and core.', TRUE),
('22222222-2222-2222-2222-222222222205', NULL, 'Barbell Bent Over Row', 'BACK', 'Biceps, Rear Deltoids', 'BARBELL', 'Hinge at hips, pull bar to lower rib cage with controlled eccentric.', TRUE),
('22222222-2222-2222-2222-222222222206', NULL, 'Pull-Up', 'BACK', 'Biceps, Forearms', 'BODYWEIGHT', 'Hang from bar with pronated grip, pull chest up toward bar, lower with control.', TRUE),
('22222222-2222-2222-2222-222222222207', NULL, 'Dumbbell Incline Bench Press', 'CHEST', 'Front Deltoids, Triceps', 'DUMBBELL', 'Set bench to 30 degrees, press dumbbells overhead with control.', TRUE),
('22222222-2222-2222-2222-222222222208', NULL, 'Romanian Deadlift', 'LEGS', 'Hamstrings, Glutes', 'BARBELL', 'Slight knee bend, hinge back pushing hips backward until hamstring stretch, drive forward.', TRUE),
('22222222-2222-2222-2222-222222222209', NULL, 'Dumbbell Lateral Raise', 'SHOULDERS', 'Traps', 'DUMBBELL', 'Raise dumbbells out to sides until parallel to floor leading with elbows.', TRUE),
('22222222-2222-2222-2222-222222222210', NULL, 'Outdoor Running', 'FULL_BODY', 'Cardiovascular', 'CARDIO', 'Steady-state aerobic or interval running.', TRUE);
