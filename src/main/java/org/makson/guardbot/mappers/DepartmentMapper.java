package org.makson.guardbot.mappers;


import org.makson.guardbot.dto.DepartmentDto;
import org.makson.guardbot.models.Department;
import org.makson.guardbot.models.DepartmentMember;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto mapDepartment(Department department);
    List<DepartmentDto> mapDepartmentMemberList(List<DepartmentMember> departmentMembers);

    default DepartmentDto mapDepartmentMembers(DepartmentMember departmentMember) {
        return new DepartmentDto(
                departmentMember.getDepartment().getId(),
                departmentMember.getDepartment().getName()
        );
    }
}
