package org.makson.guardbot.mappers;

import org.makson.guardbot.dto.DepartmentMemberDto;
import org.makson.guardbot.models.DepartmentMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = GuardsmanMapper.class)
public interface DepartmentMemberMapper {
    @Mapping(target = "guardsmanName", source = "guardsman")
    DepartmentMemberDto mapDepartmentMember(DepartmentMember departmentMember);

    List<DepartmentMemberDto> mapDepartmentMember(List<DepartmentMember> departmentMembers);
//
//    default String mapDepartmentToString(DepartmentMember departmentMember) {
//        return departmentMember.getDepartment().getName();
//    }
}
