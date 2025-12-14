package org.makson.guardbot.dto;

public record RankDto(
        int id,
        String name,
        int points,
        int special_reports,
        int position
) {
}
