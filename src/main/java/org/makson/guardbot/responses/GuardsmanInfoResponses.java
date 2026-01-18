package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.guardsman.GuardsmanInfoDto;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@BService
public class GuardsmanInfoResponses {
    public MessageEmbed replyListGuardsmen(List<GuardsmanInfoDto> guardsmen) {
        return new EmbedBuilder()
                .setTitle("Гвардейцы")
                .setColor(new Color(170, 7, 207))
                .setDescription(
                        guardsmen.stream()
                                .map(GuardsmanInfoDto::name)
                                .collect(Collectors.joining("\n"))
                )
                .build();
    }

    public MessageEmbed replyGetInfo(GuardsmanInfoDto guardsman, Color color) {
        String headGuard = "Глава гвардии";
        String deputyHeadGuard = "Зам. главы";

        String faceIconUrl = "https://mc-heads.net/avatar/%s/128";
        String description;

        if (guardsman.rankName().equals(headGuard)) {
            description = getAdminInfoDescription().formatted(
                    guardsman.rankName(),
                    getDepartment(guardsman.departmentsName()),
                    guardsman.description()
            );
        } else if (guardsman.rankName().equals(deputyHeadGuard)) {
            description = getAdminInfoDescription().formatted(
                    guardsman.rankName(),
                    getDepartment(guardsman.departmentsName()),
                    "Вес " + guardsman.points() + "кг"
            );
        } else {
            description = getRankedInfoDescription().formatted(
                    guardsman.rankName(),
                    getDepartment(guardsman.departmentsName()),
                    guardsman.points(),
                    guardsman.requiredPoints(),
                    guardsman.specialReport(),
                    guardsman.requiredSpecialReport(),
                    getLastReportDate(guardsman)
            );
        }

        return new EmbedBuilder()
                .setTitle("Информация о %s".formatted(guardsman.name()))
                .setColor(color)
                .setThumbnail(faceIconUrl.formatted(guardsman.name()))
                .setDescription(description)
                .build();
    }

    public MessageEmbed replyGuardsmenLastReport(List<GuardsmanInfoDto> guardsmen) {
        List<String> lastReports = guardsmen.stream()
                .map(guardsman ->
                        "```" + guardsman.name() + ": " + getLastReportDate(guardsman) + "```"
                )
                .toList();


        return new EmbedBuilder()
                .setTitle("Последние отчеты")
                .setColor(new Color(157, 120, 145))
                .setDescription(String.join("\n", lastReports))
                .build();
    }

    private String getRankedInfoDescription() {
        return """
                **Ранг:** %s
                **Отделы:** %s
                **Баллы:** %d/%d
                **Спец. отчеты:** %d/%d
                **Последний отчет:** %s
                """;
    }

    private String getAdminInfoDescription() {
        return """
                **Ранг:** %s
                **Отделы:** %s
                **Подробнее:** %s
                """;
    }

    private String getLastReportDate(GuardsmanInfoDto guardsman) {
        if (guardsman.lastReport() == null) {
            return "Отсутствует";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            return guardsman.lastReport().format(formatter);
        }
    }

    private String getDepartment(List<String> departments) {
        if (departments.isEmpty()) {
            return "Отсутствуют";
        }
        return String.join(", ", departments);
    }
}
