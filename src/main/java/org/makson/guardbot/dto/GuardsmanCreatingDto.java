package org.makson.guardbot.dto;

import java.time.LocalDate;
import java.util.List;

public record GuardsmanCreatingDto(
        String name,
        RankDto rank,
        LocalDate lastReport
) {
}
