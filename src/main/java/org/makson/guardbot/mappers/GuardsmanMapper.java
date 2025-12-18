package org.makson.guardbot.mappers;

import org.makson.guardbot.models.Guardsman;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartmentMemberMapper.class, RankMapper.class})
public interface GuardsmanMapper {
//    GuardsmanInfoDto mapGuardsman(Guardsman guardsman);
//
//    List<GuardsmanInfoDto> mapGuardsmanList(List<Guardsman> guardsmen);
//
    default String mapGuardsmanToString(Guardsman guardsman) {
        return guardsman.getName();
    }
}
