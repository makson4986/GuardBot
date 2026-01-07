package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.prison.PrisonerDto;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@BService
public class PrisonResponses {
    public MessageEmbed replyGetPrisonList(List<PrisonerDto> prisoners) {
        return new EmbedBuilder()
                .setTitle("Заключенные")
                .setColor(new Color(89, 95, 96))
                .setDescription(
                        prisoners.stream()
                                .map(PrisonerDto::name)
                                .collect(Collectors.joining("\n"))
                )
                .build();
    }

    public MessageEmbed replyPrisonerInfo(PrisonerDto prisonResponseDto) {
        return new EmbedBuilder()
                .setTitle("Информация о " + prisonResponseDto.name())
                .setColor(new Color(89, 95, 96))
                .setDescription("""
                        **Дата заключения:** %s
                        **Дата освобождения:** %s
                        **Тюремная камера:** №%s
                        **Причина:** %s
                        """.formatted(
                        changeDateFormat(prisonResponseDto.conclusionDate()),
                        changeDateFormat(prisonResponseDto.releaseDate()),
                        prisonResponseDto.prisonCell(),
                        prisonResponseDto.reason()
                ))
                .build();
    }

    private String changeDateFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(formatter);
    }
}
