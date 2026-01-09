package org.makson.guardbot.listener;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import org.makson.guardbot.dto.log.LogDto;
import org.makson.guardbot.utils.DiscordLogger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@BService
@RequiredArgsConstructor
public abstract class BaseReactionListener {
    protected final DiscordLogger logger;

    protected void processReaction(GenericMessageReactionEvent event,
                                   boolean isDeletion,
                                   String confirmationReaction,
                                   Consumer<Message> messageProcessor) {

        if (!isConfirmationReaction(event, confirmationReaction)) {
            return;
        }

        Message message = event.retrieveMessage().complete();

        if (isDuplication(message.getReactions(), confirmationReaction, isDeletion) || !isReport(message)) {
            return;
        }

        messageProcessor.accept(message);

        logger.info(new LogDto(
                event.retrieveUser().complete(),
                null,
                getSuccessMessage()
        ));
    }

    protected abstract String getSuccessMessage();

    protected boolean isDuplication(List<MessageReaction> reactions, String confirmationReaction, boolean isDeletion) {
        int numberReactionsWhenDeleting = 0;
        int numberReactionsWhenAddition = 1;

        Optional<MessageReaction> react = reactions.stream()
                .filter(reaction -> reaction.getEmoji().getName().equals(confirmationReaction))
                .findFirst();

        if (isDeletion) {
            return react.filter(messageReaction -> messageReaction.getCount() > numberReactionsWhenDeleting).isPresent();
        } else {
            return react.filter(messageReaction -> messageReaction.getCount() > numberReactionsWhenAddition).isPresent();

        }
    }

    protected boolean isReport(Message message) {
        MessageEmbed embedMessage = message.getEmbeds().getFirst();
        return embedMessage.getTitle() != null && embedMessage.getTitle().startsWith("Отчет");
    }

    protected boolean isConfirmationReaction(GenericMessageReactionEvent event, String confirmationReaction) {
        return event.getEmoji().getName().equals(confirmationReaction);
    }
}
