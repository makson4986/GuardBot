//package org.makson.dsbot.mappers;

//import lombok.RequiredArgsConstructor;
//import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
//import org.makson.dsbot.ReportParser;
//import org.makson.dsbot.dto.ReportDto;
//import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class ReportMapper {
//    private final ReportParser parser;
//
//    public ReportDto mapCommand(SlashCommandInteractionEvent event) {
//        return new ReportDto(
//                parser.parseUsernames(event.getOption("usernames")),
//                parser.parseType(event.getOption("type")),
//                parser.parseDescription(event.getOption("description")),
//                parser.parseMediaUrl(event.getOption("media-url")),
//                parser.parseMedia(event.getOption("media"))
//        );
//    }
//}
