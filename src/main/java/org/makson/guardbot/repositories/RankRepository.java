package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    Rank getRankByPosition(int newRankPosition);

    @Query("""
            select r from Rank r
            where r.isAchieved = true and r.position = (
                select max (r2.position) from Rank r2
                where r2.isAchieved = true
            )
            """)
    Rank getMaxRank();
}
