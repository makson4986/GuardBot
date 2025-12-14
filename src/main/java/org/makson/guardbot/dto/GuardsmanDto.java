package org.makson.guardbot.dto;

import java.util.List;

public record GuardsmanDto(
        int id,
        String name,
        RankDto rank,
        List<DepartmentDto> departments,
        int points,
        int specialReports,
        int countReports,
        int requiredPoints,
        int requiredSpecialReports
) {
}
