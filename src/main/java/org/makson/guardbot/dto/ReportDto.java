package org.makson.guardbot.dto;

public record ReportDto(
        String name,
        String reason,
        int points
) {
}
