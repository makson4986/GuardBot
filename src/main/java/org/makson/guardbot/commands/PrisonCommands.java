package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import lombok.RequiredArgsConstructor;
import org.makson.guardbot.commands.autocompletes.DepartmentAutocomplete;

@Command
@RequiredArgsConstructor
public class PrisonCommands extends ApplicationCommand {
    @JDASlashCommand(name = "get_info_prison", description = "Получить информацию о заключенных")
    public void onSlashGetInfoPrison(
            GuildSlashEvent event,
            @SlashOption(name = "department", description = "Выбери отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String department) {

    }
}
