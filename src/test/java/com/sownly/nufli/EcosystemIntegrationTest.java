package com.sownly.nufli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sownly.nufli.domain.fitness.ActivityType;
import com.sownly.nufli.domain.fitness.MuscleGroup;
import com.sownly.nufli.domain.fitness.WorkoutStatus;
import com.sownly.nufli.domain.identity.ActivityLevel;
import com.sownly.nufli.domain.identity.BiologicalSex;
import com.sownly.nufli.domain.nutrition.GoalType;
import com.sownly.nufli.domain.nutrition.MealType;
import com.sownly.nufli.presentation.web.fitness.dto.*;
import com.sownly.nufli.presentation.web.identity.dto.*;
import com.sownly.nufli.presentation.web.nutrition.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EcosystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Phase 7 Cross-App Integration Verification: Shared Auth, Profile, Weight, and Cross-Domain Visibility")
    void testFullCrossAppEcosystemJourney() throws Exception {
        String testEmail = "athlete_" + UUID.randomUUID() + "@example.com";
        String testPassword = "StrongPassword123!";

        // 1. Register account
        RegisterRequest registerReq = new RegisterRequest(testEmail, testPassword, "Alex", "Smith");
        MvcResult regResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode regNode = objectMapper.readTree(regResult.getResponse().getContentAsString());
        String tokenFromNutrity = regNode.get("accessToken").asText();
        assertNotNull(tokenFromNutrity);

        // 2. Login from Flitness using the EXACT same credentials
        LoginRequest loginReq = new LoginRequest(testEmail, testPassword);
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode loginNode = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String tokenFromFlitness = loginNode.get("accessToken").asText();
        assertNotNull(tokenFromFlitness);

        // 3. Update User Profile demographics via Nutrity
        UserProfileRequest profileReq = new UserProfileRequest(
            "Alexander", "Smith", LocalDate.of(1996, 5, 20),
            BiologicalSex.MALE, BigDecimal.valueOf(182.0), ActivityLevel.MODERATELY_ACTIVE
        );
        mockMvc.perform(put("/api/v1/users/me/profile")
                .header("Authorization", "Bearer " + tokenFromNutrity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileReq)))
            .andExpect(status().isOk());

        // Verify profile is instantly reflected when read from Flitness
        MvcResult meResult = mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", "Bearer " + tokenFromFlitness))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode meNode = objectMapper.readTree(meResult.getResponse().getContentAsString());
        assertEquals("Alexander", meNode.get("profile").get("firstName").asText());
        assertEquals(182.0, meNode.get("profile").get("heightCm").asDouble());

        // 4. Log Daily Body Weight via Nutrity
        LocalDate today = LocalDate.now();
        WeightEntryRequest weightReq = new WeightEntryRequest(today, BigDecimal.valueOf(78.50), "Morning weigh-in");
        mockMvc.perform(post("/api/v1/nutrition/weight")
                .header("Authorization", "Bearer " + tokenFromNutrity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weightReq)))
            .andExpect(status().isCreated());

        // Set Nutrition Goal (Cut: -500 kcal from calculated TDEE)
        NutritionGoalRequest goalReq = new NutritionGoalRequest(today, GoalType.CUT, null, null, null, null);
        mockMvc.perform(post("/api/v1/nutrition/goals")
                .header("Authorization", "Bearer " + tokenFromNutrity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalReq)))
            .andExpect(status().isCreated());

        // 5. Create Food & Log Meal in Nutrity
        FoodRequest foodReq = new FoodRequest(
            "Chicken & Quinoa Bowl", "MealPrep Co", "g", BigDecimal.valueOf(100.0),
            BigDecimal.valueOf(150.0), BigDecimal.valueOf(25.0), BigDecimal.valueOf(15.0), BigDecimal.valueOf(3.0), BigDecimal.valueOf(2.0)
        );
        MvcResult foodResult = mockMvc.perform(post("/api/v1/nutrition/foods")
                .header("Authorization", "Bearer " + tokenFromNutrity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodReq)))
            .andExpect(status().isCreated())
            .andReturn();

        UUID foodId = UUID.fromString(objectMapper.readTree(foodResult.getResponse().getContentAsString()).get("id").asText());

        MealRequest mealReq = new MealRequest(
            MealType.LUNCH, today, Instant.now(),
            List.of(new MealItemRequest(foodId, BigDecimal.valueOf(300.0))) // 300g = 450 kcal
        );
        mockMvc.perform(post("/api/v1/nutrition/meals")
                .header("Authorization", "Bearer " + tokenFromNutrity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealReq)))
            .andExpect(status().isCreated());

        // 6. Complete a Workout in Flitness
        // First, create an exercise
        ExerciseRequest exReq = new ExerciseRequest(
            "Incline Dumbbell Press", MuscleGroup.CHEST, "Triceps", null, "Press with 30-degree incline"
        );
        MvcResult exResult = mockMvc.perform(post("/api/v1/fitness/exercises")
                .header("Authorization", "Bearer " + tokenFromFlitness)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exReq)))
            .andExpect(status().isCreated())
            .andReturn();

        UUID exerciseId = UUID.fromString(objectMapper.readTree(exResult.getResponse().getContentAsString()).get("id").asText());

        // Start and complete a workout session: 3 sets of 10 @ 32kg
        WorkoutRequest workoutReq = new WorkoutRequest(
            null, "Chest Heavy Session", Instant.now().minusSeconds(3600), Instant.now(),
            WorkoutStatus.COMPLETED, "Great pump",
            List.of(new WorkoutExerciseRequest(
                exerciseId, 0, "Top set focus",
                List.of(
                    new ExerciseSetRequest(1, null, BigDecimal.valueOf(32.0), 10, BigDecimal.valueOf(8.5), true),
                    new ExerciseSetRequest(2, null, BigDecimal.valueOf(32.0), 10, BigDecimal.valueOf(9.0), true),
                    new ExerciseSetRequest(3, null, BigDecimal.valueOf(32.0), 8, BigDecimal.valueOf(10.0), true)
                )
            ))
        );
        mockMvc.perform(post("/api/v1/fitness/workouts")
                .header("Authorization", "Bearer " + tokenFromFlitness)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutReq)))
            .andExpect(status().isCreated());

        // Log Cardio activity in Flitness
        ActivityRequest activityReq = new ActivityRequest(
            ActivityType.RUNNING, Instant.now().minusSeconds(1800), 30, BigDecimal.valueOf(4.5), null, "Outdoor trail"
        );
        mockMvc.perform(post("/api/v1/fitness/activities")
                .header("Authorization", "Bearer " + tokenFromFlitness)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activityReq)))
            .andExpect(status().isCreated());

        // 7. Check Cross-Domain Daily Energy Balance Aggregation
        MvcResult summaryResult = mockMvc.perform(get("/api/v1/summary/daily?date=" + today)
                .header("Authorization", "Bearer " + tokenFromNutrity))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode summaryNode = objectMapper.readTree(summaryResult.getResponse().getContentAsString());
        // Nutrition checks
        assertEquals(450.0, summaryNode.get("nutrition").get("consumedCalories").asDouble());
        assertEquals(75.0, summaryNode.get("nutrition").get("consumedProtein").asDouble());
        // Fitness checks
        assertEquals(1, summaryNode.get("fitness").get("workoutsCompleted").asInt());
        assertEquals(1, summaryNode.get("fitness").get("cardioActivitiesCompleted").asInt());
        assertTrue(summaryNode.get("fitness").get("totalVolumeKg").asDouble() > 0.0);
        assertTrue(summaryNode.get("fitness").get("totalEstimatedCaloriesBurned").asDouble() > 0.0);

        // Verify Energy Balance Philosophy: Exercise calories are NOT subtracted from food budget
        assertTrue(summaryNode.get("energyBalancePhilosophy").asText().contains("Simplify your life"));
    }
}
