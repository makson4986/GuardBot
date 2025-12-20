package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.dto.ReportDto;
import org.makson.guardbot.utils.ReportParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final GuardsmanService guardsmanService;
    private final ReportParser reportParser;

    public ReportDto create(ReportDto reportDto) {
        List<String> guardsmen = reportParser.parseUsernames(reportDto.names().getFirst(), "Parameter 'usernames' is missing");


        List<GuardsmanInfoDto> guardsmenDto = guardsmen.stream()
                .map(guardsman ->
                        guardsmanService.getGuardsman(reportParser.parseIdToUsernames(guardsman))
                )
                .toList();

        guardsmenDto
                .forEach(guardsman ->
                        guardsmanService.updateLastReportDate(guardsman.name(), LocalDate.now())
                );


        return new ReportDto(
                guardsmen,
                reportParser.parseRequiredParameter(reportDto.type(), "Parameter 'type' is missing"),
                reportParser.parseOptionalParameter(reportDto.description()),
                reportParser.parseOptionalParameter(reportDto.mediaUrl()),
                reportDto.media()
        );
    }
}
