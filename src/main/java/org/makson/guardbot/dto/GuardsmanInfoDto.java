package org.makson.guardbot.dto;

import java.time.LocalDate;
import java.util.List;

public record GuardsmanInfoDto(
        int id,
        String name,
        RankDto rank,
        List<String> departmentsName,
        LocalDate lastReport,
        int points,
        int specialReport,
        int requiredPoints,
        int requiredSpecialReport
) {
}
