package org.makson.guardbot.dto;

import org.makson.guardbot.models.DepartmentRole;

public record DepartmentMemberDto(
        int id,
        String guardsmanName,
        DepartmentRole role
) {
}
