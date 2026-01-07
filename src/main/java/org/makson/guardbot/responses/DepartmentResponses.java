package org.makson.guardbot.responses;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.makson.guardbot.dto.department.DepartmentInfoDto;
import org.makson.guardbot.dto.department.DepartmentMemberResponseDto;
import org.makson.guardbot.models.DepartmentRole;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@BService
public class DepartmentResponses {
    public MessageEmbed replyInfoDepartment(DepartmentInfoDto departmentInfoDto) {
        return new EmbedBuilder()
                .setTitle("Информация об " + departmentInfoDto.name())
                .setColor(new Color(8, 205, 243))
                .setDescription("""
                        **Начальник отдела:** %s
                        **Участники:** \n%s
                        """.formatted(
                        getHeadman(departmentInfoDto.members()),
                        getDepartmentMembersString(departmentInfoDto.members())
                ))
                .build();
    }

    private String getHeadman(java.util.List<DepartmentMemberResponseDto> departments) {
        Optional<DepartmentMemberResponseDto> headman = departments.stream()
                .filter(member -> member.role() == DepartmentRole.HEADMAN)
                .findFirst();

        if (headman.isPresent()) {
            return headman.get().guardsmanName();
        }

        return "Начальник не назначен";
    }

    private String getDepartmentMembersString(List<DepartmentMemberResponseDto> members) {
        if (members.isEmpty()) {
            return "В данном отделе участники отсутствуют";
        }

        return members.stream()
                .map(DepartmentMemberResponseDto::guardsmanName)
                .collect(Collectors.joining("\n"));
    }
}
