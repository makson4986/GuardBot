package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.makson.guardbot.services.ReactionSpecialReportService;
import org.makson.guardbot.utils.DiscordLogger;

@BService
public class ReactionSpecialReportListener extends BaseReactionListener {
    private final ReactionSpecialReportService reactionSpecialReportService;

    public ReactionSpecialReportListener(DiscordLogger logger, ReactionSpecialReportService reactionSpecialReportService) {
        super(logger);
        this.reactionSpecialReportService = reactionSpecialReportService;
    }

    @BEventListener
    public void addSpecialReport(MessageReactionAddEvent event) {
        processReaction(event, false, "🌟",
                message -> reactionSpecialReportService.changeSpecialReport(message, false));
    }

    @BEventListener
    public void removeSpecialReport(MessageReactionRemoveEvent event) {
        processReaction(event, true, "🌟",
                message -> reactionSpecialReportService.changeSpecialReport(message, true));
    }

    @Override
    protected String getSuccessMessage() {
        return "The quantity special reports have been changed";
    }
}
