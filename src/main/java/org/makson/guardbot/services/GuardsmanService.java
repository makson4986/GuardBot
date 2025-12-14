package org.makson.guardbot.services;


import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.mappers.GuardsmanMapper;
import org.makson.guardbot.repositories.GuardsmanRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@BService
@RequiredArgsConstructor
public class GuardsmanService {
    private final GuardsmanRepository guardsmanRepository;
    private final GuardsmanMapper mapper;

    @Transactional()
    public GuardsmanDto getGuardsmanInfo(String name) {
        var guardsman = guardsmanRepository.findByName(name);
        return mapper.mapGuardsman(guardsman.orElseThrow(() -> new GuardsmanNotFoundException("Информации об этом гвардейце отсутсвует")));
    }

    @Transactional(readOnly = true)
    public List<GuardsmanDto> getAllGuardsman() {
        var guardsmen = guardsmanRepository.findAll();
        return mapper.mapGuardsmanList(guardsmen);
    }
}
