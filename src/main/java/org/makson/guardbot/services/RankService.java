package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.RankDto;
import org.makson.guardbot.mappers.RankMapper;
import org.makson.guardbot.models.Rank;
import org.makson.guardbot.repositories.RankRepository;
import org.springframework.transaction.annotation.Transactional;

@BService
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final RankMapper mapper;

    @Transactional(readOnly = true)
    public RankDto getRank(String name) {
        Rank rank = rankRepository.findByName(name);
        return mapper.mapRank(rank);
    }
}
