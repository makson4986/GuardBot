package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.PrisonerResponseDto;
import org.makson.guardbot.models.Prisoner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrisonerMapper {
    PrisonerResponseDto mapPrisoner(Prisoner prisoner);
    List<PrisonerResponseDto> mapListPrisoner(List<Prisoner> prisoners);
}
