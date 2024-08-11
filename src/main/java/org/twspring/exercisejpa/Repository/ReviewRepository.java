package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.twspring.exercisejpa.Model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
