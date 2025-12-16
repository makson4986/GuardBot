package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.models.Guardsman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMemberMapper.class, RankMapper.class})
public interface GuardsmanMapper {
    @Mapping(target = "requiredPoints",
            source = "guardsman",
            qualifiedByName = "requiredPoints")
    @Mapping(target = "requiredSpecialReport",
            source = "guardsman",
            qualifiedByName = "requiredSpecialReport")
    @Mapping(target = "departmentsName", source = "departments")
    GuardsmanInfoDto mapGuardsman(Guardsman guardsman);

    List<GuardsmanInfoDto> mapGuardsmanList(List<Guardsman> guardsmen);

    @Named("requiredPoints")
    default int calcRequiredPoints(Guardsman guardsman) {
        return guardsman.getRank().getMaxPoints();
    }

    @Named("requiredSpecialReport")
    default int calcRequiredSpecialReports(Guardsman guardsman) {
        return guardsman.getRank().getMaxSpecialReports();
    }
}
