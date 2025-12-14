package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class PrisonCommands extends ApplicationCommand {

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "prison", subcommand = "info", description = "Получить информацию о заключенных")
    public void onSlashGetInfoPrison(GuildSlashEvent event) {

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
