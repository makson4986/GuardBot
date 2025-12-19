package org.makson.guardbot.utils;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import org.makson.guardbot.exceptions.ReportParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReportParser {
    private Guild guild;
    @Value("${discord.guild-id}")
    private String guildId;

    @BEventListener
    public void onGuildReady(GuildReadyEvent event) {
        if (event.getGuild().getId().equals(guildId)) {
            guild = event.getGuild();
        }
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

    public List<String> parseUsernamesFromReport(String text) {
        String firstPointPattern = "^1\\s*\\.\\s*(.*)$";
        Pattern pointPattern = Pattern.compile(firstPointPattern, Pattern.MULTILINE);
        Matcher pointMatcher = pointPattern.matcher(text);

        if (!pointMatcher.find()) {
            return Collections.emptyList();
        }
        String firstPointContent = pointMatcher.group(1);

        Pattern mentionPattern = Pattern.compile("<@(\\d+)>");
        Matcher mentionMatcher = mentionPattern.matcher(firstPointContent);

        List<String> usernames = new ArrayList<>();
        while (mentionMatcher.find()) {
            usernames.add(mentionMatcher.group(0));
        }

        return usernames;
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
