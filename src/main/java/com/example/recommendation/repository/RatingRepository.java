package com.example.recommendation.repository;

import com.example.recommendation.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Rating.RatingId> {
}
