package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.makson.guardbot.dto.log.LogDto;
import org.makson.guardbot.dto.rank.RankDto;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.exceptions.RoleNotFoundException;
import org.makson.guardbot.responses.LogResponse;
import org.makson.guardbot.services.GuardsmanService;
import org.makson.guardbot.services.RankService;
import org.makson.guardbot.utils.DiscordLogger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Command
@RequiredArgsConstructor
public class GuardsmanOpCommands extends ApplicationCommand {
    private final GuardsmanService guardsmanService;
    private final RankService rankService;
    private final DiscordLogger logger;


    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "guardsmen-hire", description = "Нанять гвардейца")
    public void onSlashHireGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Имя гвардейца, которого необходимо нанять") User user
    ) {
        event.deferReply().queue();

        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(user).complete();

        if (member == null) {
            throw new GuardsmanNotFoundException("Guardsman not found");
        }

        guild.modifyMemberRoles(member, getInitialRoles(guild), Collections.emptyList()).complete();
        guardsmanService.saveGuardsman(member.getEffectiveName());

        event.getHook().sendMessage("Гвардеец" + member.getEffectiveName() + "был принят!").queue();
    }

    @JDASlashCommand(name = "guardsmen-dismiss", description = "Уволить гвардейца")
    public void onSlashDismissGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Имя гвардейца, которого необходимо уволить") User user
    ) {
        event.deferReply().queue();

        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(user).complete();

        if (member == null) {
            throw new GuardsmanNotFoundException("Guardsman not found");
        }

        guild.modifyMemberRoles(member, Collections.emptyList(), member.getRoles()).queue();
        guardsmanService.deleteGuardsman(member.getEffectiveName());

        event.getHook().sendMessage("Гвардеец " + member.getEffectiveName() + " уволен!").queue();
    }

    @JDASlashCommand(name = "guardsmen-promote", description = "Повысить должность гвардейцу на следующую в ранговой системе")
    public void onSlashPromoteGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо повысить") User user
    ) {
        event.deferReply().queue();

        Member member = event.getGuild().retrieveMember(user).complete();
        RankDto newRank = guardsmanService.changeRank(member.getEffectiveName(), false);
        changeRank(event, member, newRank);

        event.getHook().sendMessage(
                "Гвардеец " + member.getEffectiveName() + " был повышен до ранга " + newRank.name()
        ).queue();
    }

    @JDASlashCommand(name = "guardsmen-demote", description = "Понизить должность гвардейцу на предыдущую в ранговой системе")
    public void onSlashDemoteGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо понизить") User user
    ) {
        event.deferReply().queue();

        Member member = event.getGuild().retrieveMember(user).complete();
        RankDto newRank = guardsmanService.changeRank(member.getEffectiveName(), true);
        changeRank(event, member, newRank);

        event.getHook().sendMessage(
                "Гвардеец " + member.getEffectiveName() + " был понижен до ранга " + newRank.name()
        ).queue();
    }

    @JDASlashCommand(name = "guardsmen-points", description = "Изменить количество баллов")
    public void onSlashChangePointsGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "У кого необходимо изменить") User user,
            @SlashOption(name = "quantity", description = "На сколько изменить (пример 10, -15)") Integer quantity
    ) {
        event.deferReply().queue();

        Member member = event.getGuild().retrieveMember(user).complete();
        guardsmanService.changePoints(member.getEffectiveName(), quantity);

        event.getHook().sendMessage(
                "Баллы гвардейца " + member.getEffectiveName() + " были изменены на " + quantity
        ).queue();
    }

    @JDASlashCommand(name = "guardsmen-change-name", description = "Изменить имя")
    public void onSlashChangeName(
            GuildSlashEvent event,
            @SlashOption(name = "new-name", description = "Новый ник, как в minecraft") String newName,
            @Nullable @SlashOption(name = "guardsman", description = "Кому поменять ник") User user
    ) {
        event.deferReply().queue();
        Member member = defineMember(event, user);
        member.modifyNickname(newName).complete();
        guardsmanService.changeName(member.getEffectiveName(), newName);

        event.getHook().sendMessage("Имя успешно изменено!").queue();

        logger.info(new LogDto(
                member.getUser(),
                event.getCommandString(),
                "The guardsman changed his name from " + member.getEffectiveName() + " to " + newName
        ));
    }

    private List<Role> getInitialRoles(Guild guild) {
        Role intern = guild.getRolesByName("Стажёр", true).getFirst();
        Role guardAgent = guild.getRolesByName("Агент Гвардии", true).getFirst();

        if (intern == null || guardAgent == null) {
            throw new RoleNotFoundException("Role with name 'Стажер' or 'Агент Гвардии' not found");
        }

        return Arrays.asList(intern, guardAgent);
    }

    private void clearRanksRole(Guild guild, Member member) {
        List<Role> roles = rankService.getAllRanks().stream()
                .map(RankDto::name)
                .map(rank -> guild.getRolesByName(rank, true).getFirst())
                .toList();

        guild.modifyMemberRoles(member, Collections.emptyList(), roles).complete();
    }

    private void changeRank(GuildSlashEvent event, Member guardsman, RankDto newRank) {
        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(guardsman).complete();
        Role roleRank = guild.getRolesByName(newRank.name(), true).getFirst();

        clearRanksRole(guild, member);
        guild.addRoleToMember(member, roleRank).complete();
    }

    private Member defineMember(GuildSlashEvent event, User user) {
        if (user != null) {
            return event.getGuild().retrieveMember(user).complete();
        }

        return event.getMember();
    }

}
