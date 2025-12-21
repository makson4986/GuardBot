package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.makson.guardbot.dto.LogDto;
import org.makson.guardbot.services.ReactionSpecialReportService;
import org.makson.guardbot.utils.DiscordLogger;

@BService
@RequiredArgsConstructor
public class ReactionSpecialReportListener {
    private final ReactionSpecialReportService reactionSpecialReportService;
    private final DiscordLogger logger;

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void addSpecialReport(MessageReactionAddEvent event) {
        changeSpecialReport(event, false);
    }

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void removeSpecialReport(MessageReactionRemoveEvent event) {
        changeSpecialReport(event, true);
    }

    private void changeSpecialReport(GenericMessageReactionEvent event, boolean isSubtraction) {
        final String CONFIRMATION_REACTION = "🌟";

        if (!event.getEmoji().getName().equals(CONFIRMATION_REACTION)) {
            return;
        }

        Message message = event.retrieveMessage().complete();

        reactionSpecialReportService.changeSpecialReport(message, isSubtraction);

        logger.info(new LogDto(
                event.retrieveUser().complete(),
                null,
                "The quantity special reports have been changed"
        ));
    }
}
