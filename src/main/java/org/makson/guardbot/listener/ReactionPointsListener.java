package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.makson.guardbot.services.ReactionPointsService;
import org.makson.guardbot.utils.DiscordLogger;

@BService
public class ReactionPointsListener extends BaseReactionListener {
    private final ReactionPointsService reactionPointsService;

    public ReactionPointsListener(DiscordLogger logger, ReactionPointsService reactionPointsService) {
        super(logger);
        this.reactionPointsService = reactionPointsService;
    }

    @BEventListener
    public void addPoints(MessageReactionAddEvent event) {
        processReaction(event, false, "✅",
                message -> reactionPointsService.changePoints(message.getReactions(), message, false));
    }

    @BEventListener
    public void removePoints(MessageReactionRemoveEvent event) {
        processReaction(event, true, "✅",
                message -> reactionPointsService.changePoints(message.getReactions(), message, true));
    }

    @Override
    protected String getSuccessMessage() {
        return "The points have been changed";
    }
}
