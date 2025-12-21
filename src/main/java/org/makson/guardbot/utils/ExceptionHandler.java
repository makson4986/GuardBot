package org.makson.guardbot.utils;

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

import java.time.format.DateTimeParseException;

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
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Отдел не найден");
            case GuardsmanNotFoundException _ ->
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Гвардеец не найден");
            case ChannelNotFoundException _ ->
                    handleError((SlashCommandInteractionEvent) event, throwable, "Внутренняя ошибка сервера");
            case ReportParseException _ ->
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Ошибка при формировании отчета, проверьте данные");
            case PrisonerNotFoundException _ ->
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Заключенный не найден");
            case DateTimeParseException _ ->
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Неверный формат даты");
            case RoleNotFoundException _ ->
                    handleWarn((SlashCommandInteractionEvent) event, throwable, "Внутренняя ошибка сервера");
            case DepartmentMemberAlreadyExistsException _ ->
                handleWarn((SlashCommandInteractionEvent) event, throwable,"Гвардеец уже добавлен в отдел");
            case RankLimitReachedException _ ->
                handleWarn((SlashCommandInteractionEvent) event, throwable, "Гвардеец достиг максимально/минимально допустимого ранга");
            default -> {
                handleError((SlashCommandInteractionEvent) event, throwable, "Внутренняя ошибка сервера");
            }
        }
    }

    private void handleNullEvent(Throwable throwable) {
        logger.error(new LogDto(
                null,
                null,
                throwable.getMessage()
        ));
    }

    private void handleWarn(SlashCommandInteractionEvent event, Throwable throwable, String message) {
        MessageEmbed answer = embedMessageService.createErrorEmbed(message);
        logger.warn(createLogDto(event, throwable));
        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private void handleError(SlashCommandInteractionEvent event, Throwable throwable, String message) {
        MessageEmbed answer = embedMessageService.createErrorEmbed(message);
        logger.error(createLogDto(event, throwable));
        event.getHook().sendMessageEmbeds(answer).queue();
    }

    private LogDto createLogDto(SlashCommandInteractionEvent event, Throwable throwable) {
        return new LogDto(
                event.getUser(),
                event.getCommandString(),
                throwable.getMessage()
        );
    }

}
