package org.makson.guardbot.commamds;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.makson.guardbot.commamds.autocompletes.DepartmentAutocomplete;
import org.makson.guardbot.commamds.autocompletes.ReportTypeAutocomplete;

import javax.annotation.Nullable;

@Command
public class PublicCommands extends ApplicationCommand {
    @JDASlashCommand(name = "list_guardsman", description = "Список всех гвардейцев")
    public void onSlashListGuardsman(GuildSlashEvent event) {

    }

    @JDASlashCommand(name = "get_info", description = "Получить подробную информацию")
    public void onSlashGetInfo(GuildSlashEvent event,
                               @Nullable
                               @SlashOption(name = "guardsman", description = "Информацию кого необходимо получить")
                               User user) {

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
