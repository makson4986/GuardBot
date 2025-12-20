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
import org.makson.guardbot.dto.PrisonerResponseDto;
import org.makson.guardbot.services.EmbedMessageService;
import org.makson.guardbot.services.PrisonerService;

@Command
@RequiredArgsConstructor
public class PrisonCommands extends ApplicationCommand {
    private final PrisonerService prisonService;
    private final EmbedMessageService embedMessageService;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "prison", subcommand = "list", description = "Получить информацию о заключенных")
    public void onSlashGetPrisonList(GuildSlashEvent event) {

    }

    @JDASlashCommand(name = "prison", subcommand = "info", description = "Получить информацию о заключенном")
    public void onSlashGetPrisonerInfo(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Имя заключенного") String username
    ) {
        event.deferReply().queue();

        PrisonerResponseDto infoPrisoner = prisonService.getInfoPrisoner(username);
        MessageEmbed answer = embedMessageService.createInfoPrisonerEmbed(infoPrisoner);

        event.getHook().sendMessageEmbeds(answer).queue();
    }


    @JDASlashCommand(name = "prison", subcommand = "put", description = "Посадить в тюрьму")
    public void onSlashPutPrison(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Ник заключенного") String username) {

    }

    @JDASlashCommand(name = "prison", subcommand = "amend-release-date", description = "Изменить дату освобождения из тюрьмы")
    public void onSlashAmendReleaseDate(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Кому необходимо изменить срок") String username,
            @SlashOption(name = "date", description = "Дата и время в формате дд.мм.гггг") String newDate) {

    }

    @JDASlashCommand(name = "prison", subcommand = "free", description = "Освободить из тюрьмы")
    public void onSlashFree(
            GuildSlashEvent event,
            @SlashOption(name = "username", description = "Кого необходимо освободить из тюрьмы") String username) {

    }


}
