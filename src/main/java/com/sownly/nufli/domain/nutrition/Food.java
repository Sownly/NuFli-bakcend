package com.sownly.nufli.domain.nutrition;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId; // Nullable if system verified food

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(name = "serving_unit", nullable = false)
    private String servingUnit = "g";

    @Column(name = "serving_size_grams", nullable = false, precision = 8, scale = 2)
    private BigDecimal servingSizeGrams = BigDecimal.valueOf(100.0);

    @Column(name = "calories_per_100g", nullable = false, precision = 8, scale = 2)
    private BigDecimal caloriesPer100g;

    @Column(name = "protein_per_100g", nullable = false, precision = 8, scale = 2)
    private BigDecimal proteinPer100g = BigDecimal.ZERO;

    @Column(name = "carbs_per_100g", nullable = false, precision = 8, scale = 2)
    private BigDecimal carbsPer100g = BigDecimal.ZERO;

    @Column(name = "fat_per_100g", nullable = false, precision = 8, scale = 2)
    private BigDecimal fatPer100g = BigDecimal.ZERO;

    @Column(name = "fiber_per_100g", nullable = false, precision = 8, scale = 2)
    private BigDecimal fiberPer100g = BigDecimal.ZERO;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Food() {}

    public Food(UUID userId, String name, String brand, BigDecimal caloriesPer100g,
                BigDecimal proteinPer100g, BigDecimal carbsPer100g, BigDecimal fatPer100g,
                BigDecimal fiberPer100g, boolean verified) {
        this.userId = userId;
        this.name = name;
        this.brand = brand;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g != null ? proteinPer100g : BigDecimal.ZERO;
        this.carbsPer100g = carbsPer100g != null ? carbsPer100g : BigDecimal.ZERO;
        this.fatPer100g = fatPer100g != null ? fatPer100g : BigDecimal.ZERO;
        this.fiberPer100g = fiberPer100g != null ? fiberPer100g : BigDecimal.ZERO;
        this.verified = verified;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getServingUnit() { return servingUnit; }
    public void setServingUnit(String servingUnit) { this.servingUnit = servingUnit; }
    public BigDecimal getServingSizeGrams() { return servingSizeGrams; }
    public void setServingSizeGrams(BigDecimal servingSizeGrams) { this.servingSizeGrams = servingSizeGrams; }
    public BigDecimal getCaloriesPer100g() { return caloriesPer100g; }
    public void setCaloriesPer100g(BigDecimal caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    public BigDecimal getProteinPer100g() { return proteinPer100g; }
    public void setProteinPer100g(BigDecimal proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    public BigDecimal getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(BigDecimal carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    public BigDecimal getFatPer100g() { return fatPer100g; }
    public void setFatPer100g(BigDecimal fatPer100g) { this.fatPer100g = fatPer100g; }
    public BigDecimal getFiberPer100g() { return fiberPer100g; }
    public void setFiberPer100g(BigDecimal fiberPer100g) { this.fiberPer100g = fiberPer100g; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public Instant getCreatedAt() { return createdAt; }
}
