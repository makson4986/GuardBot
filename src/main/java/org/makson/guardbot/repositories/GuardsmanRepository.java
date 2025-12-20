package org.makson.guardbot.repositories;


import org.makson.guardbot.models.Guardsman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardsmanRepository extends JpaRepository<Guardsman, Integer> {
    Optional<Guardsman> findByName(String name);

    void deleteByName(String name);
}
