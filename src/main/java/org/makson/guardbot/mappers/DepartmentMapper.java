package org.makson.guardbot.mappers;


import org.makson.guardbot.dto.DepartmentInfoDto;
import org.makson.guardbot.models.Department;
import org.makson.guardbot.models.DepartmentMember;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentInfoDto mapDepartment(Department department);
    List<DepartmentInfoDto> mapDepartmentMemberList(List<DepartmentMember> departmentMembers);
}
