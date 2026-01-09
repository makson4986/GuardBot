package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.makson.guardbot.dto.guardsman.GuardCallDto;

import java.awt.*;

@BService
public class GuardCallResponses {
    public MessageEmbed replyCreateGuardCallForm() {
        return new EmbedBuilder()
                .setTitle("**Форма вызова гвардии**")
                .setDescription("""
                        1. Ник
                        2. Координаты, куда нужно прибыть гвардии
                        3. Причина вызова (PvP, CoreProtect, RP или другое)
                        4. Кратко опишите причину вызова
                        """)
                .setColor(0xFF0000)
                .setFooter("За ложный вызов предусмотрен штраф в размере 10 АР!")
                .build();
    }

    public MessageEmbed replyGuardCallModal(GuardCallDto guardCallDto) {
        return new EmbedBuilder()
                .setTitle("**Вызов гвардии**")
                .setDescription("""
                        1. %s
                        2. %s
                        3. %s
                        4. %s
                        """.formatted(
                        guardCallDto.username(),
                        guardCallDto.coordinates(),
                        guardCallDto.reason(),
                        guardCallDto.description()
                ))
                .setColor(new Color(83, 197, 14))
                .build();
    }

    public MessageEmbed replyGuardCallCancellation(User user, MessageEmbed oldEmbed) {
        return new EmbedBuilder()
                .setTitle("**Вызов отклонен **" + user.getEffectiveName())
                .setDescription(oldEmbed.getDescription())
                .setColor(oldEmbed.getColor())
                .build();
    }

    public MessageEmbed replyGuardCallAcceptance (User user, MessageEmbed oldEmbed) {
        return new EmbedBuilder()
                .setTitle("**Вызов принят **" + user.getEffectiveName())
                .setDescription(oldEmbed.getDescription())
                .setColor(oldEmbed.getColor())
                .build();
    }
}
