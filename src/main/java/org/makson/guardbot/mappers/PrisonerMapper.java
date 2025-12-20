package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.PrisonerResponseDto;
import org.makson.guardbot.models.Prisoner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrisonerMapper {
    PrisonerResponseDto mapPrisoner(Prisoner prisoner);
}
