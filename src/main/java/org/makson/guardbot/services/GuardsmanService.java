package org.makson.guardbot.services;


import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.mappers.GuardsmanMapper;
import org.makson.guardbot.models.Guardsman;
import org.makson.guardbot.repositories.GuardsmanRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@BService
@RequiredArgsConstructor
public class GuardsmanService {
    private final GuardsmanRepository guardsmanRepository;
    private final GuardsmanMapper mapper;

    @Transactional(readOnly = true)
    public GuardsmanInfoDto getGuardsman(String name) {
        Optional<Guardsman> guardsman = guardsmanRepository.findByName(name);
        return mapper.mapGuardsman(guardsman.orElseThrow(() -> new GuardsmanNotFoundException("There is no information about this guardsman.")));

    }

    @Transactional(readOnly = true)
    public List<GuardsmanInfoDto> getAllGuardsman() {
        List<Guardsman> guardsmen = guardsmanRepository.findAll();
        return mapper.mapGuardsmanList(guardsmen);
    }


//    public void saveGuardsman(GuardsmanCreatingDto guardsmanDto) {
//        Guardsman guardsman = mapper.mapGuardsmanDto(guardsmanDto);
//        guardsmanRepository.save(guardsman);
//    }

    @Transactional
    public void updateLastReportDate(String name, LocalDate date) {
        Guardsman guardsman = guardsmanRepository.findByName(name)
                .orElseThrow(() -> new GuardsmanNotFoundException("There is no information about this guardsman."));
        guardsman.setLastReport(date);
    }
}
