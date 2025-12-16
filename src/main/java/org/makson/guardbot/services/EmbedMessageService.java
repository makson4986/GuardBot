package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.DepartmentInfoDto;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.dto.ReportDto;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@BService
public class EmbedMessageService {
    public MessageEmbed createErrorEmbed(String description) {
        return new EmbedBuilder()
                .setTitle("Ошибка!")
                .setColor(0xFF0000)
                .setDescription(description)
                .build();
    }

    public MessageEmbed createRankedEmbed(GuardsmanInfoDto guardsman, Color color) {
        return createInfoEmbed(guardsman, color, """
                **Ранг:** %s
                **Отделы:** %s
                **Баллы:** %d/%d
                **Спец. отчеты:** %d/%d
                **Последний отчет:** %s
                """);
    }

    public MessageEmbed createAdminEmbed(GuardsmanInfoDto guardsman, Color color) {
        return createInfoEmbed(guardsman, color, """
                **Ранг:** %s
                **Отделы:** %s
                """);
    }

    public MessageEmbed createAllInfoEmbed(List<GuardsmanInfoDto> guardsmen) {
        return new EmbedBuilder()
                .setTitle("Информация о гвардейцах")
                .setColor(0xFF0000)
                .setDescription(
                        guardsmen.stream()
                                .map(GuardsmanInfoDto::name)
                                .collect(Collectors.joining("\n"))
                )
                .build();
    }

    public MessageEmbed createReportEmbed(ReportDto reportDto) {
        return new EmbedBuilder()
                .setTitle("Отчет от " + createReportDate())
                .setColor(0xFF0000)
                .setDescription("""
                                1. %s
                                2. %s
                                3. %s
                                
                                Дополнительные материалы:
                                %s
                                
                                """.formatted(
                                String.join(" ", reportDto.names()),
                                reportDto.type(),
                                reportDto.description(),
                                reportDto.mediaUrl()
                        )
                )
                .build();
    }

    private MessageEmbed createInfoEmbed(GuardsmanInfoDto guardsman, Color color, String descriptionTemplate) {
        String faceIconUrl = "https://mc-heads.net/avatar/%s/128";

        return new EmbedBuilder()
                .setTitle("Информация о %s".formatted(guardsman.name()))
                .setColor(color)
                .setThumbnail(faceIconUrl.formatted(guardsman.name()))
                .setDescription(descriptionTemplate.formatted(
                        guardsman.rank().name(),
                        getDepartment(guardsman.departmentsName()),
                        guardsman.points(),
                        guardsman.requiredPoints(),
                        guardsman.specialReport(),
                        guardsman.requiredSpecialReport(),
                        guardsman.lastReport().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                ))
                .build();
    }

    private String getDepartment(List<String> departments) {
        if (departments.isEmpty()) {
            return "Отсутствуют";
        }
        return String.join(", ", departments);
    }

    private String createReportDate() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
