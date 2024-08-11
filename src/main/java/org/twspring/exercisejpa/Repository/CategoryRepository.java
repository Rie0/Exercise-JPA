package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.exercisejpa.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
