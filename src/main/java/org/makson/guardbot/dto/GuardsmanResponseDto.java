package org.makson.guardbot.dto;

import java.time.LocalDate;
import java.util.List;

public record GuardsmanResponseDto(
        int id,
        String name,
        RankDto rank,
        List<DepartmentDto> departments,
        LocalDate lastReport,
        int points,
        int specialReport,
        int requiredPoints,
        int requiredSpecialReport
) {
}
