package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.exercisejpa.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
