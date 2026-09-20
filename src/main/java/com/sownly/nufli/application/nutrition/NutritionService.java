package com.sownly.nufli.application.nutrition;

import com.sownly.nufli.common.exception.BadRequestException;
import com.sownly.nufli.common.exception.ResourceNotFoundException;
import com.sownly.nufli.domain.identity.User;
import com.sownly.nufli.domain.identity.UserProfile;
import com.sownly.nufli.domain.identity.UserRepository;
import com.sownly.nufli.domain.nutrition.*;
import com.sownly.nufli.presentation.web.nutrition.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NutritionService {

    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;
    private final RecipeRepository recipeRepository;
    private final WeightEntryRepository weightEntryRepository;
    private final NutritionGoalRepository nutritionGoalRepository;
    private final UserRepository userRepository;

    public NutritionService(
        FoodRepository foodRepository,
        MealRepository mealRepository,
        RecipeRepository recipeRepository,
        WeightEntryRepository weightEntryRepository,
        NutritionGoalRepository nutritionGoalRepository,
        UserRepository userRepository
    ) {
        this.foodRepository = foodRepository;
        this.mealRepository = mealRepository;
        this.recipeRepository = recipeRepository;
        this.weightEntryRepository = weightEntryRepository;
        this.nutritionGoalRepository = nutritionGoalRepository;
        this.userRepository = userRepository;
    }

    // ---------------- Foods ----------------
    public FoodResponse createFood(UUID userId, FoodRequest request) {
        Food food = new Food(
            userId,
            request.name().trim(),
            request.brand() != null ? request.brand().trim() : null,
            request.caloriesPer100g(),
            request.proteinPer100g(),
            request.carbsPer100g(),
            request.fatPer100g(),
            request.fiberPer100g(),
            false
        );
        if (request.servingUnit() != null) food.setServingUnit(request.servingUnit());
        if (request.servingSizeGrams() != null) food.setServingSizeGrams(request.servingSizeGrams());

        return FoodResponse.from(foodRepository.save(food));
    }

    @Transactional(readOnly = true)
    public Page<FoodResponse> searchFoods(UUID userId, String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return foodRepository.searchFoods(userId, query.trim(), pageable).map(FoodResponse::from);
        }
        return foodRepository.findAllAvailable(userId, pageable).map(FoodResponse::from);
    }

    @Transactional(readOnly = true)
    public FoodResponse getFood(UUID id, UUID userId) {
        Food food = foodRepository.findByIdAndUserIdOrVerifiedTrue(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Food not found: " + id));
        return FoodResponse.from(food);
    }

    // ---------------- Meals ----------------
    public MealResponse logMeal(UUID userId, MealRequest request) {
        Meal meal = new Meal(userId, request.mealType(), request.loggedDate(), request.loggedAt());

        for (MealItemRequest itemReq : request.items()) {
            Food food = foodRepository.findByIdAndUserIdOrVerifiedTrue(itemReq.foodId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found: " + itemReq.foodId()));

            BigDecimal cal = NutritionCalculator.scaleNutrient(food.getCaloriesPer100g(), itemReq.quantityGrams());
            BigDecimal pro = NutritionCalculator.scaleNutrient(food.getProteinPer100g(), itemReq.quantityGrams());
            BigDecimal carb = NutritionCalculator.scaleNutrient(food.getCarbsPer100g(), itemReq.quantityGrams());
            BigDecimal fat = NutritionCalculator.scaleNutrient(food.getFatPer100g(), itemReq.quantityGrams());

            MealItem item = new MealItem(food, itemReq.quantityGrams(), cal, pro, carb, fat);
            meal.addItem(item);
        }

        return MealResponse.from(mealRepository.save(meal));
    }

    @Transactional(readOnly = true)
    public List<MealResponse> getMealsForDate(UUID userId, LocalDate date) {
        return mealRepository.findByUserIdAndLoggedDateWithItems(userId, date).stream()
            .map(MealResponse::from)
            .toList();
    }

    public void deleteMeal(UUID userId, UUID mealId) {
        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + mealId));
        mealRepository.delete(meal);
    }

    // ---------------- Recipes ----------------
    public RecipeResponse createRecipe(UUID userId, RecipeRequest request) {
        Recipe recipe = new Recipe(
            userId,
            request.name().trim(),
            request.description(),
            request.servings(),
            request.preparationTimeMinutes(),
            request.instructions()
        );

        BigDecimal totalCal = BigDecimal.ZERO;
        BigDecimal totalPro = BigDecimal.ZERO;
        BigDecimal totalCarb = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;

        for (RecipeIngredientRequest ingReq : request.ingredients()) {
            Food food = foodRepository.findByIdAndUserIdOrVerifiedTrue(ingReq.foodId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient food not found: " + ingReq.foodId()));

            RecipeIngredient ingredient = new RecipeIngredient(food, ingReq.quantityGrams());
            recipe.addIngredient(ingredient);

            totalCal = totalCal.add(NutritionCalculator.scaleNutrient(food.getCaloriesPer100g(), ingReq.quantityGrams()));
            totalPro = totalPro.add(NutritionCalculator.scaleNutrient(food.getProteinPer100g(), ingReq.quantityGrams()));
            totalCarb = totalCarb.add(NutritionCalculator.scaleNutrient(food.getCarbsPer100g(), ingReq.quantityGrams()));
            totalFat = totalFat.add(NutritionCalculator.scaleNutrient(food.getFatPer100g(), ingReq.quantityGrams()));
        }

        recipe.setTotalCalories(totalCal);
        recipe.setTotalProtein(totalPro);
        recipe.setTotalCarbs(totalCarb);
        recipe.setTotalFat(totalFat);

        return RecipeResponse.from(recipeRepository.save(recipe));
    }

    @Transactional(readOnly = true)
    public Page<RecipeResponse> getRecipes(UUID userId, BigDecimal maxCalories, BigDecimal minProtein, Pageable pageable) {
        return recipeRepository.findUserRecipes(userId, maxCalories, minProtein, pageable)
            .map(RecipeResponse::from);
    }

    @Transactional(readOnly = true)
    public RecipeResponse getRecipe(UUID userId, UUID recipeId) {
        Recipe recipe = recipeRepository.findByIdAndUserIdWithIngredients(recipeId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Recipe not found: " + recipeId));
        return RecipeResponse.from(recipe);
    }

    // ---------------- Weight ----------------
    public WeightEntryResponse logWeight(UUID userId, WeightEntryRequest request) {
        WeightEntry entry = weightEntryRepository.findByUserIdAndRecordedDate(userId, request.recordedDate())
            .orElse(new WeightEntry(userId, request.recordedDate(), request.weightKg(), request.notes()));

        entry.setWeightKg(request.weightKg());
        entry.setNotes(request.notes());

        return WeightEntryResponse.from(weightEntryRepository.save(entry));
    }

    @Transactional(readOnly = true)
    public List<WeightEntryResponse> getWeightHistory(UUID userId, LocalDate startDate, LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(3);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        return weightEntryRepository.findByUserIdAndRecordedDateBetweenOrderByRecordedDateAsc(userId, start, end).stream()
            .map(WeightEntryResponse::from)
            .toList();
    }

    // ---------------- Goals & Daily Summary ----------------
    public NutritionGoalResponse setGoal(UUID userId, NutritionGoalRequest request) {
        nutritionGoalRepository.deactivateAllForUser(userId);

        BigDecimal targetKcal = request.targetCalories();
        BigDecimal targetPro = request.targetProteinGrams();
        BigDecimal targetCarb = request.targetCarbsGrams();
        BigDecimal targetFat = request.targetFatGrams();

        if (targetKcal == null || targetPro == null || targetCarb == null || targetFat == null) {
            // Auto-calculate from profile and latest weight
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
            UserProfile profile = user.getProfile();
            BigDecimal weight = weightEntryRepository.findTopByUserIdOrderByRecordedDateDesc(userId)
                .map(WeightEntry::getWeightKg)
                .orElse(BigDecimal.valueOf(70.0));

            int age = 30;
            if (profile != null && profile.getBirthDate() != null) {
                age = Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
            }
            BigDecimal height = (profile != null && profile.getHeightCm() != null) ? profile.getHeightCm() : BigDecimal.valueOf(175.0);

            BigDecimal bmr = NutritionCalculator.calculateBmr(
                weight, height, age,
                profile != null ? profile.getBiologicalSex() : com.sownly.nufli.domain.identity.BiologicalSex.UNSPECIFIED
            );
            BigDecimal tdee = NutritionCalculator.calculateTdee(bmr, profile != null ? profile.getActivityLevel() : com.sownly.nufli.domain.identity.ActivityLevel.SEDENTARY);
            targetKcal = NutritionCalculator.calculateTargetCalories(tdee, request.goalType());

            NutritionCalculator.MacroTargets macros = NutritionCalculator.calculateMacroTargets(targetKcal, weight);
            targetPro = macros.proteinGrams();
            targetCarb = macros.carbsGrams();
            targetFat = macros.fatGrams();
        }

        LocalDate effDate = request.effectiveDate() != null ? request.effectiveDate() : LocalDate.now();
        NutritionGoal goal = new NutritionGoal(
            userId, effDate, targetKcal, targetPro, targetCarb, targetFat, request.goalType(), true
        );

        return NutritionGoalResponse.from(nutritionGoalRepository.save(goal));
    }

    @Transactional(readOnly = true)
    public NutritionGoalResponse getActiveGoal(UUID userId) {
        return nutritionGoalRepository.findTopByUserIdAndActiveTrueOrderByEffectiveDateDesc(userId)
            .map(NutritionGoalResponse::from)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public DailyNutritionSummaryResponse getDailySummary(UUID userId, LocalDate date) {
        List<Meal> meals = mealRepository.findByUserIdAndLoggedDateWithItems(userId, date);

        BigDecimal consumedCal = BigDecimal.ZERO;
        BigDecimal consumedPro = BigDecimal.ZERO;
        BigDecimal consumedCarb = BigDecimal.ZERO;
        BigDecimal consumedFat = BigDecimal.ZERO;

        for (Meal m : meals) {
            consumedCal = consumedCal.add(m.getTotalCalories());
            consumedPro = consumedPro.add(m.getTotalProtein());
            consumedCarb = consumedCarb.add(m.getTotalCarbs());
            consumedFat = consumedFat.add(m.getTotalFat());
        }

        NutritionGoal goal = nutritionGoalRepository.findTopByUserIdAndActiveTrueOrderByEffectiveDateDesc(userId)
            .orElse(null);

        BigDecimal targetCal = goal != null ? goal.getTargetCalories() : BigDecimal.valueOf(2000.0);
        BigDecimal targetPro = goal != null ? goal.getTargetProteinGrams() : BigDecimal.valueOf(140.0);
        BigDecimal targetCarb = goal != null ? goal.getTargetCarbsGrams() : BigDecimal.valueOf(220.0);
        BigDecimal targetFat = goal != null ? goal.getTargetFatGrams() : BigDecimal.valueOf(60.0);

        BigDecimal remainingCal = targetCal.subtract(consumedCal);

        List<MealResponse> mealResponses = meals.stream().map(MealResponse::from).toList();

        return new DailyNutritionSummaryResponse(
            date,
            consumedCal.setScale(2, RoundingMode.HALF_UP),
            targetCal.setScale(2, RoundingMode.HALF_UP),
            remainingCal.setScale(2, RoundingMode.HALF_UP),
            consumedPro.setScale(2, RoundingMode.HALF_UP),
            targetPro.setScale(2, RoundingMode.HALF_UP),
            consumedCarb.setScale(2, RoundingMode.HALF_UP),
            targetCarb.setScale(2, RoundingMode.HALF_UP),
            consumedFat.setScale(2, RoundingMode.HALF_UP),
            targetFat.setScale(2, RoundingMode.HALF_UP),
            meals.size(),
            mealResponses
        );
    }
}
