package org.makson.guardbot.dto;

import java.time.LocalDate;

public record PrisonerDto(
        String name,
        LocalDate conclusionDate,
        LocalDate releaseDate,
        int prisonCell,
        String reason
) {
}
