package org.makson.guardbot.dto.rank;

public record RankDto(
        int id,
        String name,
        int maxPoints,
        int maxSpecialReports,
        int position
) {
}
