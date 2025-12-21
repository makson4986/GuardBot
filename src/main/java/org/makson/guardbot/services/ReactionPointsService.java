package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import org.makson.guardbot.utils.ReportParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionPointsService {
    private final GuardsmanService guardsmanService;
    private final ReportParser reportParser;

    private final Map<String, Integer> POINTS = Map.ofEntries(
            Map.entry("0️⃣", 0),
            Map.entry("1️⃣", 1),
            Map.entry("2️⃣", 2),
            Map.entry("3️⃣", 3),
            Map.entry("4️⃣", 4),
            Map.entry("5️⃣", 5),
            Map.entry("6️⃣", 6),
            Map.entry("7️⃣", 7),
            Map.entry("8️⃣", 8),
            Map.entry("9️⃣", 9),
            Map.entry("🔟", 10)
    );

    public void changePoints(List<MessageReaction> reactions, Message message, boolean isDeletion) {
        MessageEmbed embedMessage = message.getEmbeds().getFirst();
        //TODO поставить не над embed галку

        if (!isReport(embedMessage) || isDuplication(reactions)) {
            return;
        }

        int points = calculatePoints(reactions, isDeletion);

        List<String> usernames = reportParser.parseUsernamesFromReport(embedMessage.getDescription()).stream()
                .map(reportParser::parseIdToUsernames)
                .toList();

        for (String username : usernames) {
            guardsmanService.changePoints(username, points);
        }
    }

    private int calculatePoints(List<MessageReaction> reactions, boolean isDeletion) {
        int points = 0;
        int quantityPointsReaction;

        if (isDeletion) {
            quantityPointsReaction = reactions.size();
        } else {
            quantityPointsReaction = reactions.size() - 1;
        }

        for (int i = 0; i < quantityPointsReaction; i++) {
            String emoji = reactions.get(i).getEmoji().getName();

            if (!POINTS.containsKey(emoji)) {
                continue;
            }

            int digit = POINTS.get(emoji);
            int power = quantityPointsReaction - 1 - i;

            points += digit * (int) Math.pow(10, power);
        }

        if (isDeletion) {
            points = points * -1;
        }
        return points;
    }

    private boolean isReport(MessageEmbed message) {
        return message.getTitle() != null && message.getTitle().startsWith("Отчет");
    }

    private boolean isDuplication(List<MessageReaction> reactions) {
        final String confirmationReaction = "✅";

        Optional<MessageReaction> react = reactions.stream()
                .filter(reaction -> reaction.getEmoji().getName().equals(confirmationReaction))
                .findFirst();

        return react.filter(messageReaction -> messageReaction.getCount() > 1).isPresent();

    }
}
