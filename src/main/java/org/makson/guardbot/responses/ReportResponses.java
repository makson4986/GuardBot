package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.report.ReportDto;
import org.makson.guardbot.dto.report.SpecialReportDto;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@BService
public class ReportResponses {
    public MessageEmbed replyCreateReport(SpecialReportDto specialReportDto) {
        return new EmbedBuilder()
                .setTitle("Отчет от " + createReportDate())
                .setColor(new Color(11, 207, 7))
                .setDescription("""
                                1. %s
                                2. %s
                                3. %s
                                
                                Дополнительные материалы:
                                %s
                                
                                """.formatted(
                                String.join(" ", specialReportDto.names()),
                                specialReportDto.type(),
                                specialReportDto.description(),
                                specialReportDto.mediaUrl()
                        )
                )
                .build();
    }

    public MessageEmbed replyIssueReport(ReportDto reportDto) {
        return new EmbedBuilder()
                .setTitle("Рапорт от " + createReportDate())
                .setColor(new Color(100, 5, 5))
                .setDescription("""
                        1. %s
                        2. %s
                        3. -%s баллов
                        """.formatted(
                        reportDto.name(),
                        reportDto.reason(),
                        reportDto.points()
                ))
                .build();
    }

    private String createReportDate() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
