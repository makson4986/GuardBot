package org.makson.guardbot;

import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.core.GlobalExceptionHandler;
import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.makson.guardbot.dto.LogDto;
import org.makson.guardbot.exceptions.*;
import org.makson.guardbot.services.EmbedMessageService;
import org.springframework.beans.factory.annotation.Value;

@BService
@RequiredArgsConstructor
public class ExceptionHandler implements GlobalExceptionHandler {
    private Guild guild;
    @Value("${discord.guild-id}")
    private String guildId;
    private final DiscordLogger logger;
    private final EmbedMessageService embedMessageService;

    @BEventListener
    public void onGuildReady(GuildReadyEvent event) {
        if (event.getGuild().getId().equals(guildId)) {
            guild = event.getGuild();
        }
    }

    @Override
    public void onException(@Nullable Event event, @NotNull Throwable throwable) {
        if (event == null) {
            handleNullEvent(throwable);
            return;
        }

        switch (throwable) {
            case DepartmentNotFoundException _ ->
                    handleDepartmentNotFoundException((SlashCommandInteractionEvent) event, throwable);
            case GuardsmanNotFoundException _ ->
                    handleGuardsmanNotFoundException((SlashCommandInteractionEvent) event, throwable);
            case ChannelNotFoundException _ ->
                    handleChannelNotFoundException((SlashCommandInteractionEvent) event, throwable);
            case ReportParseException _ -> handleReportParseException((SlashCommandInteractionEvent) event, throwable);
            case PrisonerNotFoundException _ ->
                    handlePrisonerNotFoundException((SlashCommandInteractionEvent) event, throwable);
            default -> {
            }
        }
    }

    private void handleDepartmentNotFoundException(SlashCommandInteractionEvent event, Throwable throwable) {
        MessageEmbed answer = embedMessageService.createErrorEmbed("Отдел не найден");

        logger.warn(new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        ));

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handleGuardsmanNotFoundException(SlashCommandInteractionEvent event, Throwable throwable) {
        MessageEmbed answer = embedMessageService.createErrorEmbed("Гвардеец не найден");

        logger.warn(new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        ));

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handleChannelNotFoundException(SlashCommandInteractionEvent event, Throwable throwable) {
        MessageEmbed answer = embedMessageService.createErrorEmbed("Внутренняя ошибка сервера");

        logger.error(new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        ));

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handleReportParseException(SlashCommandInteractionEvent event, Throwable throwable) {
        MessageEmbed answer = embedMessageService.createErrorEmbed("Ошибка при формировании отчета");

        logger.error(new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        ));

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handlePrisonerNotFoundException(SlashCommandInteractionEvent event, Throwable throwable) {
        MessageEmbed answer = embedMessageService.createErrorEmbed("Заключенный не найден");

        logger.error(new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        ));

        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handleNullEvent(Throwable throwable) {
        logger.error(new LogDto(
                null,
                null,
                throwable.getMessage()
        ));
    }
}
