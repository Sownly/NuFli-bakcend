package com.sownly.nufli.domain.nutrition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealRepository extends JpaRepository<Meal, UUID> {

    @Query("SELECT DISTINCT m FROM Meal m LEFT JOIN FETCH m.items i LEFT JOIN FETCH i.food WHERE m.userId = :userId AND m.loggedDate = :date ORDER BY m.loggedAt ASC")
    List<Meal> findByUserIdAndLoggedDateWithItems(@Param("userId") UUID userId, @Param("date") LocalDate date);

    Optional<Meal> findByIdAndUserId(UUID id, UUID userId);
}
