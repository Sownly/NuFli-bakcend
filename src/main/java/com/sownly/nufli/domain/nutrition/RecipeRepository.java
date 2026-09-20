package com.sownly.nufli.domain.nutrition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    @Query("SELECT r FROM Recipe r WHERE r.userId = :userId AND " +
           "(:maxCalories IS NULL OR r.totalCalories <= :maxCalories) AND " +
           "(:minProtein IS NULL OR r.totalProtein >= :minProtein) " +
           "ORDER BY r.createdAt DESC")
    Page<Recipe> findUserRecipes(
        @Param("userId") UUID userId,
        @Param("maxCalories") BigDecimal maxCalories,
        @Param("minProtein") BigDecimal minProtein,
        Pageable pageable
    );

    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN FETCH r.ingredients i LEFT JOIN FETCH i.food WHERE r.id = :id AND r.userId = :userId")
    Optional<Recipe> findByIdAndUserIdWithIngredients(@Param("id") UUID id, @Param("userId") UUID userId);
}
