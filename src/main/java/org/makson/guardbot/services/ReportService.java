package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.GuardsmanInfoDto;
import org.makson.guardbot.dto.SpecialReportDto;
import org.makson.guardbot.utils.ReportParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final GuardsmanService guardsmanService;
    private final ReportParser reportParser;

    public SpecialReportDto create(SpecialReportDto specialReportDto) {
        List<String> guardsmen = reportParser.parseUsernames(specialReportDto.names().getFirst(), "Parameter 'usernames' is missing");


        List<GuardsmanInfoDto> guardsmenDto = guardsmen.stream()
                .map(guardsman ->
                        guardsmanService.getGuardsman(reportParser.parseIdToUsernames(guardsman))
                )
                .toList();

        guardsmenDto
                .forEach(guardsman ->
                        guardsmanService.updateLastReportDate(guardsman.name(), LocalDate.now())
                );


        return new SpecialReportDto(
                guardsmen,
                reportParser.parseRequiredParameter(specialReportDto.type(), "Parameter 'type' is missing"),
                reportParser.parseOptionalParameter(specialReportDto.description()),
                reportParser.parseOptionalParameter(specialReportDto.mediaUrl()),
                specialReportDto.media()
        );
    }
}
