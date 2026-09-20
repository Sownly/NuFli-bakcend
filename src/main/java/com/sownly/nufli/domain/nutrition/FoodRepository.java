package com.sownly.nufli.domain.nutrition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {

    @Query("SELECT f FROM Food f WHERE (f.userId = :userId OR f.verified = true) AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "(f.brand IS NOT NULL AND LOWER(f.brand) LIKE LOWER(CONCAT('%', :query, '%')))) " +
           "ORDER BY f.verified DESC, f.name ASC")
    Page<Food> searchFoods(@Param("userId") UUID userId, @Param("query") String query, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.userId = :userId OR f.verified = true ORDER BY f.verified DESC, f.name ASC")
    Page<Food> findAllAvailable(@Param("userId") UUID userId, Pageable pageable);

    Optional<Food> findByIdAndUserIdOrVerifiedTrue(UUID id, UUID userId);
}
