package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import org.makson.guardbot.dto.ReportDto;
import org.makson.guardbot.exceptions.ReportParseException;

import java.util.Arrays;
import java.util.List;

@BService
public class ReportService {
    public ReportDto create(ReportDto reportDto) {
        return new ReportDto(
                parseUsernames(reportDto.names().getFirst()),
                parseReportType(reportDto.type()),
                parseDescription(reportDto.description()),
                parseMediaUrl(reportDto.mediaUrl()),
                reportDto.media()
        );
    }

    private List<String> parseUsernames(String usernames) {
        String[] usernameList = usernames.split("(?<=>)");
        return Arrays.stream(usernameList)
                .map(username -> username.replaceAll("[\\s,]+", ""))
                .toList();
    }

    private String parseReportType(String type) {
        if (type == null) {
            throw new ReportParseException("Parameter 'type' is missing");
        }

        return type.toLowerCase();
    }

    private String parseDescription(String description) {
        if (description == null) {
            return "";
        }
        return description.toLowerCase();
    }

    private String parseMediaUrl(String mediaUrl) {
        if (mediaUrl == null) {
            return "";
        }
        return mediaUrl.toLowerCase();
    }
}
