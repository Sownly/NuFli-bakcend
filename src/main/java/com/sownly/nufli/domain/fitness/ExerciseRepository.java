package com.sownly.nufli.domain.fitness;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    @Query("SELECT e FROM Exercise e WHERE (e.userId = :userId OR e.verified = true) AND " +
           "(:muscleGroup IS NULL OR e.primaryMuscleGroup = :muscleGroup) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:query IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY e.verified DESC, e.name ASC")
    Page<Exercise> searchExercises(
        @Param("userId") UUID userId,
        @Param("muscleGroup") MuscleGroup muscleGroup,
        @Param("category") ExerciseCategory category,
        @Param("query") String query,
        Pageable pageable
    );

    Optional<Exercise> findByIdAndUserIdOrVerifiedTrue(UUID id, UUID userId);
}
