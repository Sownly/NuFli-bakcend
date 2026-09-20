package com.sownly.nufli.domain.nutrition;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int servings = 1;

    @Column(name = "preparation_time_minutes", nullable = false)
    private int preparationTimeMinutes = 0;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "total_calories", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalCalories = BigDecimal.ZERO;

    @Column(name = "total_protein", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalProtein = BigDecimal.ZERO;

    @Column(name = "total_carbs", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalCarbs = BigDecimal.ZERO;

    @Column(name = "total_fat", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalFat = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() {}

    public Recipe(UUID userId, String name, String description, int servings, int preparationTimeMinutes, String instructions) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.servings = Math.max(1, servings);
        this.preparationTimeMinutes = preparationTimeMinutes;
        this.instructions = instructions;
        this.createdAt = Instant.now();
    }

    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }
    public int getPreparationTimeMinutes() { return preparationTimeMinutes; }
    public void setPreparationTimeMinutes(int preparationTimeMinutes) { this.preparationTimeMinutes = preparationTimeMinutes; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public BigDecimal getTotalCalories() { return totalCalories; }
    public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }
    public BigDecimal getTotalProtein() { return totalProtein; }
    public void setTotalProtein(BigDecimal totalProtein) { this.totalProtein = totalProtein; }
    public BigDecimal getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(BigDecimal totalCarbs) { this.totalCarbs = totalCarbs; }
    public BigDecimal getTotalFat() { return totalFat; }
    public void setTotalFat(BigDecimal totalFat) { this.totalFat = totalFat; }
    public Instant getCreatedAt() { return createdAt; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }
}
