package com.sownly.nufli.domain.fitness;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoutineRepository extends JpaRepository<Routine, UUID> {

    @Query("SELECT DISTINCT r FROM Routine r LEFT JOIN FETCH r.exercises re LEFT JOIN FETCH re.exercise WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<Routine> findByUserIdWithExercises(@Param("userId") UUID userId);

    @Query("SELECT DISTINCT r FROM Routine r LEFT JOIN FETCH r.exercises re LEFT JOIN FETCH re.exercise WHERE r.id = :id AND r.userId = :userId")
    Optional<Routine> findByIdAndUserIdWithExercises(@Param("id") UUID id, @Param("userId") UUID userId);
}
