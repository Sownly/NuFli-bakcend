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

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    Page<Activity> findByUserIdOrderByStartedAtDesc(UUID userId, Pageable pageable);

    Optional<Activity> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT a FROM Activity a WHERE a.userId = :userId AND a.startedAt >= :start AND a.startedAt < :end ORDER BY a.startedAt ASC")
    List<Activity> findByUserIdAndStartedAtBetween(
        @Param("userId") UUID userId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );
}
