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
import org.makson.guardbot.dto.guardsman.GuardsmanInfoDto;
import org.makson.guardbot.responses.GuardsmanInfoResponses;
import org.makson.guardbot.responses.LogResponse;
import org.makson.guardbot.services.GuardsmanService;

import javax.annotation.Nullable;
import java.util.List;

@Command
@RequiredArgsConstructor
public class GuardsmanInfoCommands extends ApplicationCommand {
    private final GuardsmanService guardsmanService;
    private final GuardsmanInfoResponses guardsmanInfoResponses;
    private final LogResponse logResponse;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "guardsmen-list", description = "Получение списка всех гвардейцев")
    public void onSlashListGuardsmen(GuildSlashEvent event) {
        event.deferReply().queue();
        List<GuardsmanInfoDto> allGuardsman = guardsmanService.getAllGuardsman();
        MessageEmbed response = guardsmanInfoResponses.replyListGuardsmen(allGuardsman);
        event.getHook().sendMessageEmbeds(response).queue();
    }

    @JDASlashCommand(name = "guardsmen-info", description = "Получение подробной информации о гвардейце")
    public void onSlashGetInfo(
            GuildSlashEvent event,
            @Nullable @SlashOption(name = "guardsman", description = "Имя гвардейца, информацию о котором необходимо получить") User user) {
        event.deferReply().queue();
        Member guardsman = defineMember(event, user);

        if (guardsman == null) {
            event.getHook().sendMessageEmbeds(logResponse.replyError("Для получения информации данный гвардеец должен находится на сервере")).queue();
            return;
        }

        GuardsmanInfoDto guardsmanInfo = guardsmanService.getGuardsman(guardsman.getEffectiveName());
        MessageEmbed response = guardsmanInfoResponses.replyGetInfo(guardsmanInfo, guardsman.getColor());
        event.getHook().sendMessageEmbeds(response).queue();
    }

    @JDASlashCommand(name = "guardsmen-last-report", description = "Получение списка даты последних отчетов всех гвардейцев")
    public void onSlashGuardsmenLastReport(GuildSlashEvent event) {
        event.deferReply().queue();
        List<GuardsmanInfoDto> allGuardsman = guardsmanService.getAllGuardsman();
        MessageEmbed response = guardsmanInfoResponses.replyGuardsmenLastReport(allGuardsman);
        event.getHook().sendMessageEmbeds(response).queue();
    }

    private Member defineMember(GuildSlashEvent event, User user) {
        if (user != null) {
            return event.getGuild().retrieveMember(user).complete();
        }

        return event.getMember();
    }
}
