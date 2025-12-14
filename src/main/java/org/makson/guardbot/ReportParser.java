package org.makson.guardbot;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.makson.guardbot.exceptions.ReportParseException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ReportParser {
    private Guild guild;

    @BEventListener
    public void onJDAReady(GuildReadyEvent event) {
        guild = event.getGuild();
    }

    public List<String> parseUsernames(String usernames, String errorText) {
        if (usernames.isEmpty()) {
            throw new ReportParseException(errorText);
        }

        String[] usernameList = usernames.split("(?<=>)");
        return Arrays.stream(usernameList)
                .map(username -> username.replaceAll("[\\s,]+", ""))
                .toList();
    }

    public String parseIdToUsernames(String id) {
        return guild.retrieveMemberById(id.replaceAll("[@<>]+", "")).complete().getEffectiveName();
    }

    public String parseOptionalParameter(String parameter) {
        if (parameter == null) {
            return "";
        }

        return parameter;
    }

    public String parseRequiredParameter(String parameter, String errorText) {
        if (parameter == null) {
            throw new ReportParseException(errorText);
        }

        return parameter;
    }
}
