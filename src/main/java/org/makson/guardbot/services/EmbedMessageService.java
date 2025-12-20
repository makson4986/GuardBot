package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.*;
import org.makson.guardbot.models.DepartmentRole;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public MessageEmbed createDepartmentInfoEmbed(DepartmentInfoDto departmentInfoDto) {
        return new EmbedBuilder()
                .setTitle("Информация об " + departmentInfoDto.name())
                .setColor(Color.RED)
                .setDescription("""
                        **Начальник отдела:** %s
                        **Участники:** \n%s
                        """.formatted(
                        getHeadman(departmentInfoDto.members()),
                        getDepartmentMembersString(departmentInfoDto.members())
                ))
                .build();
    }

    public MessageEmbed createInfoEmbed(GuardsmanInfoDto guardsman, Color color) {
        final Set<String> ADMIN_RANKS = Set.of("Зам. главы", "Глава гвардии");
        String faceIconUrl = "https://mc-heads.net/avatar/%s/128";
        String descriptionTemplate;

        if (ADMIN_RANKS.contains(guardsman.rankName())) {
            descriptionTemplate = getAdminInfoDescription();
        } else {
            descriptionTemplate = getRankedInfoDescription();
        }

        return new EmbedBuilder()
                .setTitle("Информация о %s".formatted(guardsman.name()))
                .setColor(color)
                .setThumbnail(faceIconUrl.formatted(guardsman.name()))
                .setDescription(descriptionTemplate.formatted(
                        guardsman.rankName(),
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

    private String getHeadman(List<DepartmentMemberDto> departments) {
        Optional<DepartmentMemberDto> headman = departments.stream()
                .filter(member -> member.role() == DepartmentRole.HEADMAN)
                .findFirst();

        if (headman.isPresent()) {
            return headman.get().guardsmanName();
        }

        return "Начальник не назначен";
    }

    private String getDepartmentMembersString(List<DepartmentMemberDto> members) {
        if (members.isEmpty()) {
            return "В данном отделе участники отсутсвуют";
        }

        return members.stream()
                .map(DepartmentMemberDto::guardsmanName)
                .collect(Collectors.joining("\n"));
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
                """;
    }

}
