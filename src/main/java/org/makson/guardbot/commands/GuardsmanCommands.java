package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.services.EmbedMessageService;
import org.makson.guardbot.services.GuardsmanService;

import javax.annotation.Nullable;

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

        MessageEmbed answer = null;

        try {
            GuardsmanInfoDto guardsmanInfo = guardsmanService.getGuardsman(guardsman.getEffectiveName());
            answer = replyMessageService.createInfoEmbed(guardsmanInfo, guardsman.getColor());
        } catch (Exception e) {
            answer = replyMessageService.createErrorEmbed(guardsman.getEffectiveName() + " не найден");
        }

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    @JDASlashCommand(name = "guardsmen", subcommand = "hire", description = "Нанять")
    public void onSlashHireGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо нанять") User guardsman
    ) {

    }

    @JDASlashCommand(name = "guardsmen", subcommand = "dismiss", description = "Уволить")
    public void onSlashDismissGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо уволить") User guardsman
    ) {

    }

    @JDASlashCommand(name = "guardsmen", subcommand = "promote", description = "Повысить должность")
    public void onSlashPromoteGuardsman(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Кого необходимо повысить") User guardsman
    ) {
        Member member = event.getGuild().retrieveMember(guardsman).complete();
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
            @SlashOption(name = "quantity", description = "На сколько изменить, (пример 10, -15)") Integer quantity
    ) {

    }

    private Member defineMember(GuildSlashEvent event, User user) {
        if (user != null) {
            return event.getGuild().retrieveMember(user).complete();
        }

        return event.getMember();
    }

}
