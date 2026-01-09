package org.makson.guardbot.dto.guardsman;

public record GuardCallDto(
        String username,
        String coordinates,
        String reason,
        String description
) {
}
