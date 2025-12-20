package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.PrisonerDto;
import org.makson.guardbot.models.Prisoner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrisonerMapper {
    PrisonerDto mapPrisoner(Prisoner prisoner);

    List<PrisonerDto> mapListPrisoner(List<Prisoner> prisoners);

    Prisoner mapPrisonerDto(PrisonerDto prisonerDto);
}
