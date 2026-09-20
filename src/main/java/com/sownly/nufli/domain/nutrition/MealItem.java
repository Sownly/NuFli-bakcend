package com.sownly.nufli.domain.nutrition;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "quantity_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal quantityGrams;

    @Column(name = "calculated_calories", nullable = false, precision = 8, scale = 2)
    private BigDecimal calculatedCalories;

    @Column(name = "calculated_protein", nullable = false, precision = 8, scale = 2)
    private BigDecimal calculatedProtein;

    @Column(name = "calculated_carbs", nullable = false, precision = 8, scale = 2)
    private BigDecimal calculatedCarbs;

    @Column(name = "calculated_fat", nullable = false, precision = 8, scale = 2)
    private BigDecimal calculatedFat;

    public MealItem() {}

    public MealItem(Food food, BigDecimal quantityGrams, BigDecimal calculatedCalories,
                    BigDecimal calculatedProtein, BigDecimal calculatedCarbs, BigDecimal calculatedFat) {
        this.food = food;
        this.quantityGrams = quantityGrams;
        this.calculatedCalories = calculatedCalories;
        this.calculatedProtein = calculatedProtein;
        this.calculatedCarbs = calculatedCarbs;
        this.calculatedFat = calculatedFat;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Meal getMeal() { return meal; }
    public void setMeal(Meal meal) { this.meal = meal; }
    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
    public BigDecimal getQuantityGrams() { return quantityGrams; }
    public void setQuantityGrams(BigDecimal quantityGrams) { this.quantityGrams = quantityGrams; }
    public BigDecimal getCalculatedCalories() { return calculatedCalories; }
    public void setCalculatedCalories(BigDecimal calculatedCalories) { this.calculatedCalories = calculatedCalories; }
    public BigDecimal getCalculatedProtein() { return calculatedProtein; }
    public void setCalculatedProtein(BigDecimal calculatedProtein) { this.calculatedProtein = calculatedProtein; }
    public BigDecimal getCalculatedCarbs() { return calculatedCarbs; }
    public void setCalculatedCarbs(BigDecimal calculatedCarbs) { this.calculatedCarbs = calculatedCarbs; }
    public BigDecimal getCalculatedFat() { return calculatedFat; }
    public void setCalculatedFat(BigDecimal calculatedFat) { this.calculatedFat = calculatedFat; }
}
