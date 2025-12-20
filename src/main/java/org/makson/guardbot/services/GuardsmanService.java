package org.makson.guardbot.services;


import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.models.Guardsman;
import org.makson.guardbot.repositories.GuardsmanRepository;
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


//    public void saveGuardsman(GuardsmanCreatingDto guardsmanDto) {
//        Guardsman guardsman = mapper.mapGuardsmanDto(guardsmanDto);
//        guardsmanRepository.save(guardsman);
//    }

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
}
