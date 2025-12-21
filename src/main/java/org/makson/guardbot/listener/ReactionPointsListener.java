package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.makson.guardbot.dto.LogDto;
import org.makson.guardbot.services.ReactionPointsService;
import org.makson.guardbot.utils.DiscordLogger;

import java.util.List;

@BService
@RequiredArgsConstructor
public class ReactionPointsListener {
    private final ReactionPointsService reactionPointsService;
    private final DiscordLogger logger;

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void addPoints(MessageReactionAddEvent event) {
        changePoints(event, false);
    }

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void removePoints(MessageReactionRemoveEvent event) {
        changePoints(event, true);
    }

    private void changePoints(GenericMessageReactionEvent event, boolean isDeletion) {
        final String CONFIRMATION_REACTION = "✅";

        if (!event.getEmoji().getName().equals(CONFIRMATION_REACTION)) {
            return;
        }

        Message message = event.retrieveMessage().complete();
        List<MessageReaction> reactions = message.getReactions();

        reactionPointsService.changePoints(reactions, message, isDeletion);

        logger.info(new LogDto(
                event.retrieveUser().complete(),
                null,
                "The points have been changed"
        ));
    }
}
