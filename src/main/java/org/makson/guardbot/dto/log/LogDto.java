package org.makson.guardbot.dto.log;

import net.dv8tion.jda.api.entities.User;

public record LogDto(
        User user,
        String command,
        String description
) {
}
