package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import lombok.RequiredArgsConstructor;

@Command
@RequiredArgsConstructor
public class DepartmentCommands extends ApplicationCommand {
    @JDASlashCommand(name = "get_info_department", description = "Получить информацию об отделе")
    public void onSlashGetInfoDepartment(GuildSlashEvent event) {

    }
}
