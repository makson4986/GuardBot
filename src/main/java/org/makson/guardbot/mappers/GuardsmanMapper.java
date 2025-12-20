package org.makson.guardbot.mappers;

import org.makson.guardbot.models.Guardsman;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartmentMemberMapper.class})
public interface GuardsmanMapper {
    default String mapGuardsmanToString(Guardsman guardsman) {
        return guardsman.getName();
    }
}
