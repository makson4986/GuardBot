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
import org.makson.guardbot.commands.autocompletes.DepartmentAutocomplete;
import org.makson.guardbot.dto.DepartmentInfoDto;
import org.makson.guardbot.services.DepartmentService;
import org.makson.guardbot.services.EmbedMessageService;

@Command
@RequiredArgsConstructor
public class DepartmentCommands extends ApplicationCommand {
    private final DepartmentService departmentService;
    private final EmbedMessageService embedMessageService;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "department", subcommand = "info", description = "Получить информацию об отделе")
    public void onSlashGetInfoDepartment(
            GuildSlashEvent event,
            @SlashOption(name = "name", description = "Выбери отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String name) {
        event.deferReply().queue();

        DepartmentInfoDto department = departmentService.getDepartmentByName(name);
        MessageEmbed departmentInfoEmbed = embedMessageService.createDepartmentInfoEmbed(department);

        event.getHook().sendMessageEmbeds(departmentInfoEmbed).queue();
    }
}
