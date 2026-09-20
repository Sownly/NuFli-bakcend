package com.sownly.nufli.domain.nutrition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeightEntryRepository extends JpaRepository<WeightEntry, UUID> {

    List<WeightEntry> findByUserIdAndRecordedDateBetweenOrderByRecordedDateAsc(UUID userId, LocalDate startDate, LocalDate endDate);

    Optional<WeightEntry> findByUserIdAndRecordedDate(UUID userId, LocalDate recordedDate);

    Optional<WeightEntry> findTopByUserIdOrderByRecordedDateDesc(UUID userId);
}
