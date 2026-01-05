package org.makson.guardbot.listener;

import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;
import java.util.Optional;

public interface ReactionListener {
    default boolean isDuplication(List<MessageReaction> reactions, String confirmationReaction, boolean isDeletion) {
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
}
