package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.PrisonerDto;
import org.makson.guardbot.exceptions.PrisonerNotFoundException;
import org.makson.guardbot.mappers.PrisonerMapper;
import org.makson.guardbot.models.Prisoner;
import org.makson.guardbot.repositories.PrisonerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrisonerService {
    private final PrisonerRepository prisonRepository;
    private final PrisonerMapper mapper;

    @Transactional(readOnly = true)
    public PrisonerDto getInfoPrisoner(String username) {
        Optional<Prisoner> maybePrisoner = prisonRepository.findByName(username);

        if (maybePrisoner.isEmpty()) {
            throw new PrisonerNotFoundException("Prisoner with name " + username + " not found");
        }

        return mapper.mapPrisoner(maybePrisoner.get());
    }

    @Transactional(readOnly = true)
    public List<PrisonerDto> getAllPrisoners() {
        List<Prisoner> prisoners = prisonRepository.findAll();
        return mapper.mapListPrisoner(prisoners);
    }

    @Transactional()
    public void savePrisoner(PrisonerDto prisonerDto) {
        Prisoner prisoner = mapper.mapPrisonerDto(prisonerDto);
        prisonRepository.save(prisoner);
    }
}
