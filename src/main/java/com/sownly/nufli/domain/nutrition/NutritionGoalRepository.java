package com.sownly.nufli.domain.nutrition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NutritionGoalRepository extends JpaRepository<NutritionGoal, UUID> {

    Optional<NutritionGoal> findTopByUserIdAndActiveTrueOrderByEffectiveDateDesc(UUID userId);

    @Modifying
    @Query("UPDATE NutritionGoal g SET g.active = false WHERE g.userId = :userId")
    void deactivateAllForUser(@Param("userId") UUID userId);
}
