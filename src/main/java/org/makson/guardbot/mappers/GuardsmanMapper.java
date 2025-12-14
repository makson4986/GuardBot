package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.GuardsmanDto;
import org.makson.guardbot.mappers.DepartmentMapper;
import org.makson.guardbot.mappers.RankMapper;
import org.makson.guardbot.models.Guardsman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, RankMapper.class})
public interface GuardsmanMapper {
    @Mapping(target = "requiredPoints",
            source = "guardsman",
            qualifiedByName = "requiredPoints")
    @Mapping(target = "requiredSpecialReports",
            source = "guardsman",
            qualifiedByName = "requiredSpecialReports")
    GuardsmanDto mapGuardsman(Guardsman guardsman);

    List<GuardsmanDto> mapGuardsmanList(List<Guardsman> guardsmen);


    @Named("requiredPoints")
    default int calcRequiredPoints(Guardsman guardsman) {
        return guardsman.getRank().getMaxPoints();
    }

    @Named("requiredSpecialReports")
    default int calcRequiredSpecialReports(Guardsman guardsman) {
        return guardsman.getRank().getMaxSpecialReports();
    }
}
