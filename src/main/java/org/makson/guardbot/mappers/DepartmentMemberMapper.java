package org.makson.guardbot.mappers;

import org.makson.guardbot.models.DepartmentMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMemberMapper {
    default String mapDepartmentName(DepartmentMember departmentMember) {
        return departmentMember.getDepartment().getName();
    }
}
