package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.utils.ReportParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionSpecialReportService {
    private final GuardsmanService guardsmanService;
    private final ReportParser reportParser;

    public void changeSpecialReport(Message message, boolean isSubtraction) {
        int DEFAULT_SPECIAL_REPORT_INCREMENT = 1;
        int quantity = 0;

        if (isSubtraction) {
            quantity = DEFAULT_SPECIAL_REPORT_INCREMENT * -1;
        } else {
            quantity = DEFAULT_SPECIAL_REPORT_INCREMENT;
        }


        MessageEmbed embedMessage = message.getEmbeds().getFirst();
        List<String> usernames = reportParser.parseUsernamesFromReport(embedMessage.getDescription()).stream()
                .map(reportParser::parseIdToUsernames)
                .toList();

        for (String username : usernames) {
            guardsmanService.changeSpecialReport(username, quantity);
        }
    }
}
