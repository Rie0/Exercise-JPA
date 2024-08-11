package org.twspring.exercisejpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.exercisejpa.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
