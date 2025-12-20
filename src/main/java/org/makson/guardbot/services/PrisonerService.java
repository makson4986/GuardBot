package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.PrisonerResponseDto;
import org.makson.guardbot.exceptions.PrisonerNotFoundException;
import org.makson.guardbot.mappers.PrisonerMapper;
import org.makson.guardbot.models.Prisoner;
import org.makson.guardbot.repositories.PrisonerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrisonerService {
    private final PrisonerRepository prisonRepository;
    private final PrisonerMapper mapper;

    public PrisonerResponseDto getInfoPrisoner(String username) {
        Optional<Prisoner> maybePrisoner = prisonRepository.findByName(username);

        if (maybePrisoner.isEmpty()) {
            throw new PrisonerNotFoundException("Prisoner with name " + username + " not found");
        }

        return mapper.mapPrisoner(maybePrisoner.get());
    }
}
