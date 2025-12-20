package org.makson.guardbot.dto;

import java.time.LocalDate;

public record PrisonerResponseDto(
        int id,
        String name,
        LocalDate conclusionDate,
        LocalDate releaseDate,
        int prisonCell,
        String reason
) {
}
