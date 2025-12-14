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
import org.makson.guardbot.dto.GuardsmanDto;
import org.makson.guardbot.services.GuardsmanService;
import org.makson.guardbot.services.ReplyMessageService;

import javax.annotation.Nullable;
import java.util.Set;

@Command
@RequiredArgsConstructor
public class GuardsmanCommands extends ApplicationCommand {
    private final GuardsmanService guardsmanService;
    private final ReplyMessageService replyMessageService;
    private final Set<String> ADMIN_RANKS = Set.of("Зам. главы", "Глава гвардии");

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "guardsmen", subcommand = "list", description = "Список всех гвардейцев")
    public void onSlashListGuardsmen(GuildSlashEvent event) {
        event.deferReply(false).queue();
        var allGuardsman = guardsmanService.getAllGuardsman();
        event.getHook().sendMessageEmbeds(replyMessageService.createAllInfoEmbed(allGuardsman)).queue();
    }

    @JDASlashCommand(name = "guardsmen", subcommand = "info", description = "Получить подробную информацию")
    public void onSlashGetInfo(GuildSlashEvent event,
                               @Nullable
                               @SlashOption(name = "guardsman", description = "Информацию кого необходимо получить")
                               User user) {

        event.deferReply().queue();
        Member guardsman = event.getMember();

        if (user != null) {
            guardsman = event.getGuild().retrieveMember(user).complete();
        }

        if (guardsman == null) {
            event.getHook().sendMessageEmbeds(
                            replyMessageService
                                    .createErrorEmbed("Для получения информации данный гвардеец должен находится на сервере, либо используйтся команду /dismiss_guardsman"))
                    .queue();
            return;
        }

        GuardsmanDto guardsmanInfo = guardsmanService.getGuardsmanInfo(guardsman.getEffectiveName());
        MessageEmbed answer;


        if (ADMIN_RANKS.contains(guardsmanInfo.rank().name())) {
            answer = replyMessageService.createAdminEmbed(guardsmanInfo);
        } else {
            answer = replyMessageService.createRankedEmbed(guardsmanInfo);
        }

        event.getHook().sendMessageEmbeds(answer).queue();
    }
}
