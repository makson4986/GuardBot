package org.makson.guardbot.dto;

import org.makson.guardbot.models.DepartmentRole;

public record DepartmentMemberDto(
        int id,
        GuardsmanInfoDto guardsman,
        DepartmentInfoDto department,
        DepartmentRole role
) {
}
