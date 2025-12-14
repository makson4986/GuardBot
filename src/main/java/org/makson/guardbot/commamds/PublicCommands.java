package org.makson.guardbot.commamds;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.makson.guardbot.commamds.autocompletes.DepartmentAutocomplete;
import org.makson.guardbot.commamds.autocompletes.ReportTypeAutocomplete;
import org.makson.guardbot.dto.GuardsmanDto;
import org.makson.guardbot.services.GuardsmanService;
import org.makson.guardbot.services.ReplyMessageService;

import javax.annotation.Nullable;
import java.util.Set;

@Command
@RequiredArgsConstructor
public class PublicCommands extends ApplicationCommand {
    private final GuardsmanService guardsmanService;
    private final ReplyMessageService replyMessageService;
    private final Set<String> ADMIN_RANKS = Set.of("Зам. главы", "Глава гвардии");

    @JDASlashCommand(name = "list_guardsman", description = "Список всех гвардейцев")
    public void onSlashListGuardsman(GuildSlashEvent event) {

    }

    @JDASlashCommand(name = "get_info", description = "Получить подробную информацию")
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

    @JDASlashCommand(name = "get_info_prison", description = "Получить информацию о заключенных")
    public void onSlashGetInfoPrison(
            GuildSlashEvent event,
            @SlashOption(name = "department", description = "Выбери отдел", autocomplete = DepartmentAutocomplete.DEPARTMENT_AUTOCOMPLETE_NAME) String department) {

    }

    @JDASlashCommand(name = "get_info_department", description = "Получить информацию об отделе")
    public void onSlashGetInfoDepartment(GuildSlashEvent event) {

    }

    @JDASlashCommand(name = "create_report", description = "Создать отчет")
    public void onSlashCreateReport(GuildSlashEvent event,
                                    @SlashOption(name = "usernames", description = "Ник(и) игрока(ов) предоставляющих отчет") String usernames,
                                    @SlashOption(name = "type", description = "Сфера деятельности", autocomplete = ReportTypeAutocomplete.REPORT_TYPE_AUTOCOMPLETE_NAME) String type,
                                    @Nullable @SlashOption(name = "description", description = "Подробности") String description,
                                    @Nullable @SlashOption(name = "media", description = "Фото/видео доказательства") Message.Attachment attachment,
                                    @Nullable @SlashOption(name = "media-url", description = "Ссылка на фото/видео") String mediaUrl) {
    }
}
