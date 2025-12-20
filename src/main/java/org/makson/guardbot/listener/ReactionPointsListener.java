package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.makson.guardbot.DiscordLogger;
import org.makson.guardbot.dto.LogDto;
import org.makson.guardbot.services.ReactionPointsService;

import java.util.List;

@BService
@RequiredArgsConstructor
public class ReactionPointsListener {
    private final ReactionPointsService reactionPointsService;
    private final DiscordLogger logger;

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void addPoints(MessageReactionAddEvent event) {
        final String confirmationReaction = "✅";

        if (!event.getEmoji().getName().equals(confirmationReaction)) {
            return;
        }

        Message message = event.retrieveMessage().complete();
        List<MessageReaction> reactions = message.getReactions();

        reactionPointsService.addPoints(reactions, message);

        logger.info(new LogDto(
                event.getUser(),
                null,
                "Баллы были добавлены"
        ));
    }

    @BEventListener(mode = BEventListener.RunMode.ASYNC)
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
    }
}
