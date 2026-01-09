package org.makson.guardbot.utils;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.makson.guardbot.dto.log.LogDto;
import org.makson.guardbot.exceptions.ChannelNotFoundException;
import org.makson.guardbot.responses.LogResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordLogger {
    private Guild guild;
    @Value("${discord.guild-id}")
    private String guildId;
    @Value("${discord.channels-id.log}")
    private String logChannelId;
    private final LogResponse embedMessageService;

    @BEventListener
    public void onGuildReady(GuildReadyEvent event) {
        if (event.getGuild().getId().equals(guildId)) {
            guild = event.getGuild();
        }
    }

    public void info(LogDto logDto) {
        createLog(logDto, "INFO");
    }

    public void warn(LogDto logDto) {
        createLog(logDto, "WARN");
    }

    public void error(LogDto logDto) {
        createLog(logDto, "ERROR");
    }

    private void createLog(LogDto logDto, String level) {
        TextChannel logChannel = guild.getTextChannelById(logChannelId);

        if (logChannel == null) {
            throw new ChannelNotFoundException("The channel with the specified ID was not found");
        }

        logChannel.sendMessageEmbeds(embedMessageService.createLog(logDto, level)).queue();
    }
}
