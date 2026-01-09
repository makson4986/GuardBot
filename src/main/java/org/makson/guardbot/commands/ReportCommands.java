package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.makson.guardbot.commands.autocompletes.ReportTypeAutocomplete;
import org.makson.guardbot.dto.report.ReportDto;
import org.makson.guardbot.dto.report.SpecialReportDto;
import org.makson.guardbot.exceptions.ChannelNotFoundException;
import org.makson.guardbot.responses.ReportResponses;
import org.makson.guardbot.services.GuardsmanService;
import org.makson.guardbot.services.ReportService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Command
@RequiredArgsConstructor
public class ReportCommands extends ApplicationCommand {
    @Value("${discord.special-report-channel-id}")
    private String specialReportChannelId;
    @Value("${discord.report-channel-id}")
    private String reportChannelId;
    private final ReportService reportService;
    private final GuardsmanService guardsmanService;
    private final ReportResponses reportResponses;


    @TopLevelSlashCommandData(scope = CommandScope.GUILD)
    @JDASlashCommand(name = "report-create", description = "Создать отчет")
    public void onSlashCreateReport(
            GuildSlashEvent event,
            @SlashOption(name = "usernames", description = "Ник(и) игрока(ов) подающего(щих) отчет") String usernames,
            @SlashOption(name = "type", description = "Сфера деятельности", autocomplete = ReportTypeAutocomplete.REPORT_TYPE_AUTOCOMPLETE_NAME) String type,
            @Nullable @SlashOption(name = "description", description = "Подробности") String description,
            @Nullable @SlashOption(name = "media", description = "Фото/видео доказательства") Message.Attachment attachment,
            @Nullable @SlashOption(name = "media-url", description = "Ссылка на фото/видео") String mediaUrl) {
        event.deferReply().queue();
        TextChannel reportChannel = event.getGuild().getTextChannelById(specialReportChannelId);

        if (reportChannel == null) {
            throw new ChannelNotFoundException("The channel for special reports with the specified ID was not found");
        }

        SpecialReportDto specialReportDto = new SpecialReportDto(List.of(usernames), type, description, mediaUrl, Optional.ofNullable(attachment));
        SpecialReportDto parsedReport = reportService.create(specialReportDto);
        MessageEmbed reportEmbed = reportResponses.replyCreateReport(parsedReport);

        reportChannel.sendMessageEmbeds(reportEmbed).queue();

        if (parsedReport.media().isPresent()) {
            Message.Attachment media = parsedReport.media().get();
            reportChannel.sendFiles(
                    media.getProxy().downloadAsFileUpload(media.getFileName())
            ).queue();
        }

        event.getHook().sendMessage("Отчет отправлен!").queue();
    }

    @JDASlashCommand(name = "issue-report", description = "Выдать рапорт")
    public void onSlashIssueReport(
            GuildSlashEvent event,
            @SlashOption(name = "guardsman", description = "Имя гвардейца, которому выдать рапорт") User user,
            @SlashOption(name = "reason", description = "Причина нарушения") String reason,
            @SlashOption(name = "points", description = "Количество снимаемых баллов") int points) {
        event.deferReply().queue();

        Guild guild = event.getGuild();
        Member member = guild.retrieveMember(user).complete();
        TextChannel reportChannel = guild.getTextChannelById(reportChannelId);

        if (reportChannel == null) {
            throw new ChannelNotFoundException("The channel for report with the specified ID was not found");
        }

        guardsmanService.changePoints(member.getEffectiveName(), points * -1);
        MessageEmbed answer = reportResponses.replyIssueReport(new ReportDto(member.getEffectiveName(), reason, points));
        reportChannel.sendMessageEmbeds(answer).queue();

        event.getHook().sendMessage("Рапорт отправлен!").queue();
    }
}
