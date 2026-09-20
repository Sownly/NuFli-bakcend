package com.sownly.nufli.domain.fitness;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    @Query("SELECT DISTINCT w FROM Workout w LEFT JOIN FETCH w.exercises we LEFT JOIN FETCH we.exercise LEFT JOIN FETCH we.sets WHERE w.id = :id AND w.userId = :userId")
    Optional<Workout> findByIdAndUserIdWithDetails(@Param("id") UUID id, @Param("userId") UUID userId);

    Page<Workout> findByUserIdOrderByStartedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT DISTINCT w FROM Workout w LEFT JOIN FETCH w.exercises we LEFT JOIN FETCH we.sets WHERE w.userId = :userId AND w.startedAt >= :start AND w.startedAt < :end ORDER BY w.startedAt ASC")
    List<Workout> findByUserIdAndStartedAtBetweenWithDetails(
        @Param("userId") UUID userId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );
}
