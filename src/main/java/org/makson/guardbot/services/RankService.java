package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.rank.RankDto;
import org.makson.guardbot.mappers.RankMapper;
import org.makson.guardbot.models.Rank;
import org.makson.guardbot.repositories.RankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final RankMapper mapper;

    public List<RankDto> getAllRanks() {
        List<Rank> ranks = rankRepository.findAll();
        return mapper.mapRankList(ranks);
    }
}
