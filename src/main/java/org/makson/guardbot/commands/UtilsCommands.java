package org.makson.guardbot.commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.makson.guardbot.responses.GuardCallResponses;

@Command
@RequiredArgsConstructor
public class UtilsCommands extends ApplicationCommand {
    private final GuardCallResponses guardCallResponses;

    @JDASlashCommand(name = "create-guard-call-form", description = "Создать форму вызова гвардии")
    public void onSlashCreateGuardCallForm(
            GuildSlashEvent event,
            @SlashOption(name = "channel", description = "В каком канале необходимо создать форму вызова") TextChannel channel
    ) {
        event.deferReply(true).queue();

        MessageEmbed response = guardCallResponses.replyCreateGuardCallForm();
        channel.sendMessageEmbeds(response)
                .addComponents(ActionRow.of(
                        Button.danger("guard_call", "Вызвать гвардию")
                ))
                .queue();

        event.getHook().sendMessage("Форма вызова гвардии создана").queue();
    }
}
