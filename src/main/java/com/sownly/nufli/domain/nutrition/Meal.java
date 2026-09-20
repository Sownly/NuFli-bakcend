package com.sownly.nufli.domain.nutrition;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "logged_date", nullable = false)
    private LocalDate loggedDate;

    @Column(name = "logged_at", nullable = false)
    private Instant loggedAt = Instant.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MealItem> items = new ArrayList<>();

    public Meal() {}

    public Meal(UUID userId, MealType mealType, LocalDate loggedDate, Instant loggedAt) {
        this.userId = userId;
        this.mealType = mealType;
        this.loggedDate = loggedDate;
        this.loggedAt = loggedAt != null ? loggedAt : Instant.now();
        this.createdAt = Instant.now();
    }

    public void addItem(MealItem item) {
        items.add(item);
        item.setMeal(this);
    }

    public void removeItem(MealItem item) {
        items.remove(item);
        item.setMeal(null);
    }

    public BigDecimal getTotalCalories() {
        return items.stream()
            .map(MealItem::getCalculatedCalories)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalProtein() {
        return items.stream()
            .map(MealItem::getCalculatedProtein)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCarbs() {
        return items.stream()
            .map(MealItem::getCalculatedCarbs)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalFat() {
        return items.stream()
            .map(MealItem::getCalculatedFat)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    public LocalDate getLoggedDate() { return loggedDate; }
    public void setLoggedDate(LocalDate loggedDate) { this.loggedDate = loggedDate; }
    public Instant getLoggedAt() { return loggedAt; }
    public void setLoggedAt(Instant loggedAt) { this.loggedAt = loggedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public List<MealItem> getItems() { return items; }
    public void setItems(List<MealItem> items) { this.items = items; }
}
