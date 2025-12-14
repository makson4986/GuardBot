package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Prison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrisonRepository extends JpaRepository<Prison, Integer> {
}
