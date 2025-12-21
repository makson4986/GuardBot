package org.makson.guardbot.dto;

import org.makson.guardbot.models.DepartmentRole;

public record DepartmentMemberCreatingDto(
        String guardsmanName,
        String departmentName,
        DepartmentRole role
) {
}
