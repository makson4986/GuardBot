package org.makson.guardbot.dto;

import java.util.List;

public record DepartmentInfoDto(
        int id,
        String name,
        List<DepartmentMemberDto> members
) {
}
