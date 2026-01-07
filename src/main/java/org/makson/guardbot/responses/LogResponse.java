package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.log.LogDto;

import java.awt.*;
import java.time.LocalDateTime;

@BService
public class LogResponse {
    public MessageEmbed replyError(String description) {
        return new EmbedBuilder()
                .setTitle("Ошибка!")
                .setColor(0xFF0000)
                .setDescription(description)
                .build();
    }

    public MessageEmbed createLog(LogDto logDto, String level) {
        Color logColor;

        switch (level) {
            case "WARN" -> logColor = Color.ORANGE;
            case "ERROR" -> logColor = Color.RED;
            default -> logColor = Color.GREEN;
        }

        return new EmbedBuilder()
                .setTitle(LocalDateTime.now().toString())
                .setColor(logColor)
                .setDescription(createLogText(logDto, level))
                .build();
    }

    private String createLogText(LogDto logDto, String level) {
        String commandString;

        if (logDto.command() != null) {
            commandString = "**Command:** ```" + logDto.command() + "```";
        } else {
            commandString = "";
        }

        return """
                **Level:** %s
                **User:** %s
                %s
                **Description:** ```%s
                """.formatted(
                level,
                logDto.user(),
                commandString,
                logDto.description() + "```"
        );
    }
}
