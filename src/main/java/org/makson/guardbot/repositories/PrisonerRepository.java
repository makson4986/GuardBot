package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Prisoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrisonerRepository extends JpaRepository<Prisoner, Integer> {
    Optional<Prisoner> findByName(String username);

    void deleteByName(String name);
}
