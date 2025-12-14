//package org.makson.guardbot.services;
//
//import io.github.freya022.botcommands.api.core.service.annotations.BService;
//import lombok.RequiredArgsConstructor;
//
//@BService
//@RequiredArgsConstructor
//public class ReportService {
//    private final ReplyMessageService replyMessageService;
//    private final ReportChannelProvider reportChannelProvider;
//
//    public void create(ReportDto reportDto) {
//        MessageEmbed report = replyMessageService.createReportEmbed(reportDto);
//
//        TextChannel reportChannel = reportChannelProvider.getReportChannel();
//        reportChannel.sendMessageEmbeds(report).queue();
//
//        if (reportDto.media().isPresent()) {
//            Message.Attachment media = reportDto.media().get();
//
//            reportChannel.sendFiles(
//                            media.getProxy().downloadAsFileUpload(media.getFileName())
//                    )
//                    .queue();
//        }
//    }
//
//
//}
