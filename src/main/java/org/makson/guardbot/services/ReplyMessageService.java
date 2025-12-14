package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.DepartmentDto;
import org.makson.guardbot.dto.GuardsmanDto;
import org.makson.guardbot.dto.ReportDto;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@BService
public class ReplyMessageService {
    public MessageEmbed createErrorEmbed(String description) {
        return new EmbedBuilder()
                .setTitle("Ошибка!")
                .setColor(0xFF0000)
                .setDescription(description)
                .build();
    }

    public MessageEmbed createRankedEmbed(GuardsmanDto guardsman, Color color) {
        return createInfoEmbed(guardsman, color, """
                **Ранг:** %s
                **Отделы:** %s
                **Баллы:** %d/%d
                **Спец. отчеты:** %d/%d
                **Последний отчет:**
                """);
    }

    public MessageEmbed createAdminEmbed(GuardsmanDto guardsman, Color color) {
        return createInfoEmbed(guardsman, color, """
                **Ранг:** %s
                **Отделы:** %s
                """);
    }

    public MessageEmbed createAllInfoEmbed(List<GuardsmanDto> guardsmen) {
        return new EmbedBuilder()
                .setTitle("Информация о гвардейцах")
                .setColor(0xFF0000)
                .setDescription(
                        guardsmen.stream()
                                .map(GuardsmanDto::name)
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

    private MessageEmbed createInfoEmbed(GuardsmanDto guardsman, Color color, String descriptionTemplate) {
        String faceIconUrl = "https://mc-heads.net/avatar/%s/128";

        return new EmbedBuilder()
                .setTitle("Информация о %s".formatted(guardsman.name()))
                .setColor(color)
                .setThumbnail(faceIconUrl.formatted(guardsman.name()))
                .setDescription(descriptionTemplate.formatted(
                        guardsman.rank().name(),
                        getDepartment(guardsman.departments()),
                        guardsman.points(),
                        guardsman.requiredPoints(),
                        guardsman.specialReports(),
                        guardsman.requiredSpecialReports()
                ))
                .build();
    }

    private String getDepartment(List<DepartmentDto> departments) {
        if (departments.isEmpty()) {
            return "Отсутствуют";
        }
        return departments.stream()
                .map(DepartmentDto::name)
                .collect(Collectors.joining(", "));
    }

    private String createReportDate() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
