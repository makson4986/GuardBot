package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.makson.guardbot.dto.guardsman.GuardCallDto;
import org.makson.guardbot.dto.log.LogDto;
import org.makson.guardbot.exceptions.ChannelNotFoundException;
import org.makson.guardbot.exceptions.GuildNotFoundException;
import org.makson.guardbot.exceptions.ModalFieldIsEmptyException;
import org.makson.guardbot.responses.GuardCallResponses;
import org.makson.guardbot.services.GuardCallService;
import org.makson.guardbot.utils.DiscordLogger;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

@Handler
@RequiredArgsConstructor
public class GuarCallButtonListener {
    @Value("${discord.guild-id}")
    private String guildId;
    @Value("${discord.channels-id.guard-call}")
    private String guardCallChannelId;
    @Value("${discord.roles.core-protect}")
    private String coreProtectRoleId;
    @Value("${discord.roles.agent_guard}")
    private String agentGuardRoleId;
    private final GuardCallResponses guardCallResponses;
    private final GuardCallService guardCallService;
    private final DiscordLogger logger;

    @BEventListener
    public void onGuardCallButton(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("guard_call")) {
            return;
        }

        Modal guardCallModal = guardCallService.createGuardCallModal();
        event.replyModal(guardCallModal).queue();
    }

    @BEventListener
    public void onGuardCallAccept(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("accept_call")) {
            return;
        }

        MessageEmbed oldEmbed = event.getMessage().getEmbeds().getFirst();
        MessageEmbed newEmbed = guardCallResponses.replyGuardCallAcceptance(event.getUser(), oldEmbed);
        event.editMessageEmbeds(newEmbed)
                .setComponents(Collections.emptyList())
                .queue();
    }

    @BEventListener
    public void onGuardCallCancel(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("cancel_call")) {
            return;
        }

        MessageEmbed oldEmbed = event.getMessage().getEmbeds().getFirst();
        MessageEmbed newEmbed = guardCallResponses.replyGuardCallCancellation(event.getUser(), oldEmbed);
        event.editMessageEmbeds(newEmbed)
                .setComponents(Collections.emptyList())
                .queue();
    }

    @BEventListener
    public void onGuardCallModal(ModalInteractionEvent event) {
        if (!event.getModalId().equals("guard_call_modal")) {
            return;
        }

        event.deferReply().queue();
        GuardCallDto guardCallDto;

        try {
            guardCallDto = new GuardCallDto(
                    guardCallService.checkModalField(event.getValue("username")),
                    guardCallService.checkModalField(event.getValue("coordinates")),
                    guardCallService.checkSelectMenu(event.getValue("reason")),
                    guardCallService.checkModalField(event.getValue("description"))
            );
        } catch (ModalFieldIsEmptyException e) {
            event.reply("Ошибка переданы не все параметры!").setEphemeral(true).queue();
            return;
        }

        TextChannel channel = getChannelForGuardCall(event);
        pingGuard(channel, guardCallDto.reason());
        channel.sendMessageEmbeds(guardCallResponses.replyGuardCallModal(guardCallDto))
                .addComponents(
                        ActionRow.of(
                                Button.success("accept_call", "Принять ✅"),
                                Button.danger("cancel_call", "Отклонить ❌")
                        )
                )
                .queue();

        logger.info(new LogDto(event.getUser(), null, "Called the guard"));
        event.getHook().deleteOriginal().queue();
    }

    private TextChannel getChannelForGuardCall(ModalInteractionEvent event) {
        Guild guild = event.getJDA().getGuildById(guildId);

        if (guild == null) {
            throw new GuildNotFoundException("An invalid ID was specified or the bot was not added to the guild");
        }

        TextChannel channel = guild.getTextChannelById(guardCallChannelId);

        if (channel == null) {
            throw new ChannelNotFoundException("The channel with the specified ID was not found");
        }

        return channel;
    }

    private void pingGuard(TextChannel channel, String reason) {
        if (reason.equals("CoreProtect")) {
            channel.sendMessage("<@&" + coreProtectRoleId + ">").queue();
        } else {
            channel.sendMessage("<@&" + agentGuardRoleId + ">").queue();
        }
    }
}
