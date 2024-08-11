package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.exercisejpa.Model.MerchantStock;

@Repository
public interface MerchantStockRepository extends JpaRepository<MerchantStock, Integer> {
}
