package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.makson.guardbot.commands.autocompletes.ReportTypeAutocomplete;
import org.makson.guardbot.dto.ReportDto;
import org.makson.guardbot.exceptions.ChannelNotFoundException;
import org.makson.guardbot.services.EmbedMessageService;
import org.makson.guardbot.services.ReportService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@Command
@RequiredArgsConstructor
public class ReportCommands extends ApplicationCommand {
    @Value("${discord.report-channel-id}")
    private String reportChannelId;
    private final EmbedMessageService replyMessageService;
    private final ReportService reportService;

    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "report", subcommand = "create", description = "Создать отчет")
    public void onSlashCreateReport(
            GuildSlashEvent event,
            @SlashOption(name = "usernames", description = "Ник(и) игрока(ов) предоставляющих отчет") String usernames,
            @SlashOption(name = "type", description = "Сфера деятельности", autocomplete = ReportTypeAutocomplete.REPORT_TYPE_AUTOCOMPLETE_NAME) String type,
            @Nullable @SlashOption(name = "description", description = "Подробности") String description,
            @Nullable @SlashOption(name = "media", description = "Фото/видео доказательства") Message.Attachment attachment,
            @Nullable @SlashOption(name = "media-url", description = "Ссылка на фото/видео") String mediaUrl) {
        event.deferReply().queue();
        TextChannel reportChannel = event.getGuild().getTextChannelById(reportChannelId);

        if (reportChannel == null) {
            throw new ChannelNotFoundException("The channel with the specified ID was not found");
        }

        ReportDto reportDto = new ReportDto(List.of(usernames), type, description, mediaUrl, Optional.ofNullable(attachment));

        ReportDto parsedReport = null;

        try {
            parsedReport = reportService.create(reportDto);
        } catch (Exception e) {
            event.getHook().sendMessageEmbeds(
                    replyMessageService.createErrorEmbed("Указаны неверные ник(и) гвардейцев")
            ).queue();
            return;
        }

        MessageEmbed reportEmbed = replyMessageService.createReportEmbed(parsedReport);

        reportChannel.sendMessageEmbeds(reportEmbed).queue();

        if (parsedReport.media().isPresent()) {
            Message.Attachment media = parsedReport.media().get();
            reportChannel.sendFiles(
                    media.getProxy().downloadAsFileUpload(media.getFileName())
            ).queue();
        }

        event.getHook().sendMessage("Отчет отправлен!").queue();
    }
}
