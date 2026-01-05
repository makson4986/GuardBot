package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.department.DepartmentMemberResponseDto;
import org.makson.guardbot.models.DepartmentMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = GuardsmanMapper.class)
public interface DepartmentMemberMapper {
    @Mapping(target = "guardsmanName", source = "guardsman")
    DepartmentMemberResponseDto mapDepartmentMember(DepartmentMember departmentMember);

    List<DepartmentMemberResponseDto> mapDepartmentMember(List<DepartmentMember> departmentMembers);
}
