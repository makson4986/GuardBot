package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.*;
import org.makson.guardbot.commands.autocompletes.DepartmentAutocomplete;
import org.makson.guardbot.commands.autocompletes.DepartmentRoleAutocomplete;
import org.makson.guardbot.dto.DepartmentInfoDto;
import org.makson.guardbot.dto.DepartmentMemberCreatingDto;
import org.makson.guardbot.models.DepartmentRole;
import org.makson.guardbot.services.DepartmentMembersService;
import org.makson.guardbot.services.DepartmentService;
import org.makson.guardbot.services.EmbedMessageService;

@Command
@RequiredArgsConstructor
public class DepartmentCommands extends ApplicationCommand {
    private final DepartmentService departmentService;
    private final DepartmentMembersService departmentMembersService;
    private final EmbedMessageService embedMessageService;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "department-info", description = "Получить информацию об отделе")
    public void onSlashGetInfoDepartment(
            GuildSlashEvent event,
            @SlashOption(name = "name", description = "Выбери отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String name) {
        event.deferReply().queue();

        DepartmentInfoDto department = departmentService.getDepartmentByName(name);
        MessageEmbed departmentInfoEmbed = embedMessageService.createDepartmentInfoEmbed(department);

        event.getHook().sendMessageEmbeds(departmentInfoEmbed).queue();
    }

    @JDASlashCommand(name = "department-add-member", description = "Добавить в отдел")
    public void onSlashAddToDepartment(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого добавить в отдел") User user,
            @SlashOption(name = "department-name", description = "Выберите отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String departmentName,
            @SlashOption(name = "role", description = "Выберите роль", autocomplete = DepartmentRoleAutocomplete.DEPARTMENT_ROLE_AUTOCOMPLETE_NAME) String role
    ) {
        event.deferReply().queue();

        Member member = event.getGuild().retrieveMember(user).complete();

        DepartmentRole departmentRole;

        if (role.equals("Глава")) {
            departmentRole = DepartmentRole.HEADMAN;
        } else {
            departmentRole = DepartmentRole.EMPLOYEE;
        }

        DepartmentMemberCreatingDto memberDto = new DepartmentMemberCreatingDto(
                member.getEffectiveName(),
                departmentName,
                departmentRole
        );

        departmentMembersService.addMemberToDepartment(memberDto);
        Guild guild = event.getGuild();

        Role departmentRoleDs = guild.getRolesByName(departmentName, true).getFirst();
        guild.addRoleToMember(member, departmentRoleDs).queue();

        event.getHook().sendMessage("Гвардеец добавлен в отдел").queue();
    }

    @JDASlashCommand(name = "department-remove-member", description = "Удалить из отдела")
    public void onSlashDeleteFromDepartment(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого удалить из отдела") User user,
            @SlashOption(name = "department-name", description = "Выберите отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String departmentName
    ) {
        event.deferReply().queue();

        Member member = event.getGuild().retrieveMember(user).complete();

        departmentMembersService.deleteMemberFromDepartment(member.getEffectiveName(), departmentName);
        Guild guild = event.getGuild();

        Role departmentRoleDs = guild.getRolesByName(departmentName, true).getFirst();
        guild.removeRoleFromMember(member, departmentRoleDs).queue();

        event.getHook().sendMessage("Гвардеец удален из отдела").queue();
    }
}
