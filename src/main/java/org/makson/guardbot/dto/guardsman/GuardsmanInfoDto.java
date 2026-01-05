package org.makson.guardbot.dto.guardsman;

import java.time.LocalDate;
import java.util.List;

public record GuardsmanInfoDto(
        int id,
        String name,
        String rankName,
        List<String> departmentsName,
        LocalDate lastReport,
        String description,
        int points,
        int specialReport,
        int requiredPoints,
        int requiredSpecialReport
) {
}
