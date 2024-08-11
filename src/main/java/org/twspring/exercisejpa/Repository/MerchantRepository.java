package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.exercisejpa.Model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {
}
