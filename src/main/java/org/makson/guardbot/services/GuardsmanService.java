package org.makson.guardbot.services;


import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.dto.RankDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.exceptions.RankLimitReachedException;
import org.makson.guardbot.mappers.RankMapper;
import org.makson.guardbot.models.Guardsman;
import org.makson.guardbot.models.Rank;
import org.makson.guardbot.repositories.GuardsmanRepository;
import org.makson.guardbot.repositories.RankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuardsmanService {
    private final DepartmentService departmentService;
    private final GuardsmanRepository guardsmanRepository;
    private final RankRepository rankRepository;
    private final RankMapper mapper;

    @Transactional(readOnly = true)
    public GuardsmanInfoDto getGuardsman(String name) {
        Optional<Guardsman> guardsman = guardsmanRepository.findByName(name);

        if (guardsman.isEmpty()) {
            throw new GuardsmanNotFoundException("Guardsman with name " + name + " not found");
        }

        Guardsman guard = guardsman.get();
        return createGuardsmanInfo(guard);
    }

    @Transactional(readOnly = true)
    public List<GuardsmanInfoDto> getAllGuardsman() {
        List<Guardsman> guardsmen = guardsmanRepository.findAll();
        return guardsmen.stream()
                .map(this::createGuardsmanInfo)
                .toList();
    }

    @Transactional
    public void updateLastReportDate(String name, LocalDate date) {
        Guardsman guardsman = guardsmanRepository.findByName(name)
                .orElseThrow(() -> new GuardsmanNotFoundException("Guardsman with name " + name + " not found"));
        guardsman.setLastReport(date);
    }

    @Transactional
    public void changePoints(String name, int points) {
        Guardsman guardsman = guardsmanRepository.findByName(name)
                .orElseThrow(() -> new GuardsmanNotFoundException("Guardsman with name " + name + " not found"));
        guardsman.setPoints(guardsman.getPoints() + points);
    }

    @Transactional
    public void deleteGuardsman(String name) {
        guardsmanRepository.deleteByName(name);
    }

    @Transactional
    public void saveGuardsman(String name) {
        int defaultRankId = 1;

        Rank rank = rankRepository.getReferenceById(defaultRankId);

        Guardsman guardsman = Guardsman.builder()
                .name(name)
                .rank(rank)
                .build();

        guardsmanRepository.save(guardsman);
    }

    @Transactional
    public RankDto changeRank(String name, boolean isDemotion) {
        Optional<Guardsman> guardsman = guardsmanRepository.findByName(name);

        if (guardsman.isEmpty()) {
            throw new GuardsmanNotFoundException("Guardsman with name " + name + " not found");
        }

        Guardsman guard = guardsman.get();

        int newRankPosition = defineNewRankPosition(isDemotion, guard);

        checkLimitRank(newRankPosition);

        Rank rank = rankRepository.getRankByPosition(newRankPosition);

        guard.setRank(rank);

        return mapper.mapRank(rank);
    }

    private int defineNewRankPosition(boolean isDemotion, Guardsman guardsman) {
        int currentRankPosition = guardsman.getRank().getPosition();
        int newRankPosition;


        if (isDemotion) {
            newRankPosition = currentRankPosition - 1;
        } else {
            newRankPosition = currentRankPosition + 1;
        }
        return newRankPosition;
    }

    private GuardsmanInfoDto createGuardsmanInfo(Guardsman guardsman) {
        return new GuardsmanInfoDto(
                guardsman.getId(),
                guardsman.getName(),
                guardsman.getRank().getName(),
                departmentService.findAllByGuardsmanName(guardsman.getName()),
                guardsman.getLastReport(),
                guardsman.getPoints(),
                guardsman.getSpecialReport(),
                guardsman.getRank().getMaxPoints(),
                guardsman.getRank().getMaxSpecialReports()
        );
    }

    private void checkLimitRank(int currentPosition) {
        int MAX_POSITION_RANK = 6;
        int MIN_POSITION_RANK = 1;

        if (currentPosition < MIN_POSITION_RANK || currentPosition > MAX_POSITION_RANK) {
            throw new RankLimitReachedException("The current rank has reached the limit");
        }
    }
}
