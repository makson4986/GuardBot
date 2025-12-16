package org.makson.guardbot.dto;

public record RankDto(
        int id,
        String name,
        int maxPoints,
        int maxSpecialReports,
        int position,
        long discordRoleId
) {
}
