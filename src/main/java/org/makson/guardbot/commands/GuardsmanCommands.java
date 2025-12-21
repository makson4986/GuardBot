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
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.exceptions.RoleNotFoundException;
import org.makson.guardbot.services.EmbedMessageService;
import org.makson.guardbot.services.GuardsmanService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Command
@RequiredArgsConstructor
public class GuardsmanCommands extends ApplicationCommand {
    private final GuardsmanService guardsmanService;
    private final EmbedMessageService replyMessageService;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "guardsmen", subcommand = "list", description = "Список всех гвардейцев")
    public void onSlashListGuardsmen(GuildSlashEvent event) {
        event.deferReply(false).queue();
        var allGuardsman = guardsmanService.getAllGuardsman();
        event.getHook().sendMessageEmbeds(replyMessageService.createAllInfoEmbed(allGuardsman)).queue();
    }

    @JDASlashCommand(name = "guardsmen", subcommand = "info", description = "Получить подробную информацию")
    public void onSlashGetInfo(
            GuildSlashEvent event,
            @Nullable
            @SlashOption(name = "guardsman", description = "Информацию кого необходимо получить")
            User user) {

        event.deferReply().queue();
        Member guardsman = defineMember(event, user);

        if (guardsman == null) {
            event.getHook().sendMessageEmbeds(replyMessageService.createErrorEmbed("Для получения информации данный гвардеец должен находится на сервере")).queue();
            return;
        }

        GuardsmanInfoDto guardsmanInfo = guardsmanService.getGuardsman(guardsman.getEffectiveName());
        MessageEmbed answer = replyMessageService.createInfoEmbed(guardsmanInfo, guardsman.getColor());

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    @JDASlashCommand(name = "guardsmen", subcommand = "hire", description = "Нанять")
    public void onSlashHireGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо нанять") User guardsman
    ) {
        event.deferReply().queue();

        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(guardsman).complete();

        if (member == null) {
            throw new GuardsmanNotFoundException("Guardsman not found");
        }

        guild.modifyMemberRoles(member, getInitialRoles(guild), Collections.emptyList()).complete();

        guardsmanService.saveGuardsman(guardsman.getEffectiveName());

        event.getHook().sendMessage("Гвардеец был принят!").queue();

    }

    @JDASlashCommand(name = "guardsmen", subcommand = "dismiss", description = "Уволить")
    public void onSlashDismissGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо уволить") User guardsman
    ) {
        event.deferReply().queue();

        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(guardsman).complete();

        if (member == null) {
            throw new GuardsmanNotFoundException("Guardsman not found");
        }

        guild.modifyMemberRoles(member, Collections.emptyList(), member.getRoles()).queue();
        guardsmanService.deleteGuardsman(guardsman.getEffectiveName());

        event.getHook().sendMessage("Гвардеец " + guardsman.getEffectiveName() + " уволен!").queue();
    }

    @JDASlashCommand(name = "guardsmen", subcommand = "promote", description = "Повысить должность")
    public void onSlashPromoteGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо повысить") User guardsman
    ) {

    }

    @JDASlashCommand(name = "guardsmen", subcommand = "demote", description = "Понизить должность")
    public void onSlashDemoteGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо понизить") User guardsman
    ) {

    }

    @JDASlashCommand(name = "guardsmen", subcommand = "points", description = "Изменить количество баллов")
    public void onSlashChangePointsGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "У кого необходимо изменить") User guardsman,
            @SlashOption(name = "quantity", description = "На сколько изменить (пример 10, -15)") Integer quantity
    ) {
        event.deferReply().queue();

        guardsmanService.changePoints(guardsman.getEffectiveName(), quantity);

        event.getHook().sendMessage("Баллы были изменены").queue();
    }

    private Member defineMember(GuildSlashEvent event, User user) {
        if (user != null) {
            return event.getGuild().retrieveMember(user).complete();
        }

        return event.getMember();
    }

    private List<Role> getInitialRoles(Guild guild) {
        Role intern = guild.getRolesByName("Стажер", true).getFirst();
        Role guardAgent = guild.getRolesByName("Агент Гвардии", true).getFirst();

        if (intern == null || guardAgent == null) {
            throw new RoleNotFoundException("Role with name 'Стажер' or 'Агент Гвардии' not found");
        }

        return Arrays.asList(intern, guardAgent);
    }

}
