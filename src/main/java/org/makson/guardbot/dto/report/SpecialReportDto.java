package org.makson.guardbot.dto.report;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Optional;

public record SpecialReportDto(
        List<String> names,
        String type,
        String description,
        String mediaUrl,
        Optional<Message.Attachment> media
) {
}
