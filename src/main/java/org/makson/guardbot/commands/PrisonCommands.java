package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.prison.PrisonerDto;
import org.makson.guardbot.responses.PrisonResponses;
import org.makson.guardbot.services.PrisonerService;

import java.time.LocalDate;
import java.util.List;

@Command
@RequiredArgsConstructor
public class PrisonCommands extends ApplicationCommand {
    private final PrisonerService prisonService;
    private final PrisonResponses prisonResponses;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "prison-list", description = "Получение подробной информации о заключенных")
    public void onSlashGetPrisonList(GuildSlashEvent event) {
        event.deferReply().queue();
        List<PrisonerDto> prisoners = prisonService.getAllPrisoners();
        MessageEmbed answer = prisonResponses.replyGetPrisonList(prisoners);
        event.getHook().sendMessageEmbeds(answer).queue();
    }

    @JDASlashCommand(name = "prison-info", description = "Получение подробной информации о заключенном")
    public void onSlashGetPrisonerInfo(
            GuildSlashEvent event,
            @SlashOption(name = "prisoner", description = "Имя заключенного") String prisoner
    ) {
        event.deferReply().queue();
        PrisonerDto infoPrisoner = prisonService.getInfoPrisoner(prisoner);
        MessageEmbed answer = prisonResponses.replyPrisonerInfo(infoPrisoner);
        event.getHook().sendMessageEmbeds(answer).queue();
    }


    @JDASlashCommand(name = "prison-put", description = "Посадить в тюрьму")
    public void onSlashPutPrison(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Ник заключенного") String username,
            @SlashOption(name = "release-date", description = "Дата освобождения (в формате гггг-мм-дд)") String releaseDate,
            @SlashOption(name = "prison-cell", description = "Номер тюремной камеры") Integer prisonCell,
            @SlashOption(name = "reason", description = "Причина") String reason
    ) {
        event.deferReply().queue();

        PrisonerDto prisonerDto = new PrisonerDto(
                username,
                LocalDate.parse(releaseDate),
                LocalDate.now(),
                prisonCell,
                reason
        );

        prisonService.savePrisoner(prisonerDto);
        event.getHook().sendMessage("Игрок ```" + username + "``` посажен в тюрьму!").queue();
    }

    @JDASlashCommand(name = "prison-amend-release-date", description = "Изменить дату освобождения из тюрьмы")
    public void onSlashAmendReleaseDate(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Имя заключенного") String username,
            @SlashOption(name = "date", description = "Новая дата освобождения (в формате гггг-мм-дд)") String newDate) {
        event.deferReply().queue();
        prisonService.changeReleaseDate(username, LocalDate.parse(newDate));
        event.getHook().sendMessage("Срок заключения игрока ```" + username + "``` был изменен").queue();
    }

    @JDASlashCommand(name = "prison-free", description = "Освободить заключенного из тюрьмы")
    public void onSlashFree(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Кого необходимо освободить из тюрьмы") String username) {
        event.deferReply().queue();
        prisonService.deleteByName(username);
        event.getHook().sendMessage("Игрок ```" + username + "``` освобожден!").queue();
    }
}
