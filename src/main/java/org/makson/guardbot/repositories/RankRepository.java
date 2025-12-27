package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    Rank getRankByPosition(int newRankPosition);
}
