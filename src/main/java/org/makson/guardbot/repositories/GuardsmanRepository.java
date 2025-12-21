package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Guardsman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuardsmanRepository extends JpaRepository<Guardsman, Integer> {
    Optional<Guardsman> findByName(String name);

    @Modifying
    @Query("delete from Guardsman g WHERE g.name = :name")
    void deleteByName(@Param("name") String name);
}
