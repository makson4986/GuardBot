package org.makson.guardbot.dto.report;

public record ReportDto(
        String name,
        String reason,
        int points
) {
}
