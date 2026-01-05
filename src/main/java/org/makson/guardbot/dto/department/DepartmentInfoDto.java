package org.makson.guardbot.dto.department;

import java.util.List;

public record DepartmentInfoDto(
        int id,
        String name,
        List<DepartmentMemberResponseDto> members
) {
}
