package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.DepartmentMemberDto;
import org.makson.guardbot.mappers.DepartmentMemberMapper;
import org.makson.guardbot.models.DepartmentMember;
import org.makson.guardbot.repositories.DepartmentMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentMembersService {
    private final DepartmentMemberRepository departmentMemberRepository;
    private final DepartmentMemberMapper mapper;

    @Transactional(readOnly = true)
    public List<DepartmentMemberDto> findAllByDepartmentName(String departmentName) {
        List<DepartmentMember> members = departmentMemberRepository.findAllByDepartmentName(departmentName);
        return mapper.mapDepartmentMember(members);
    }

    @Transactional(readOnly = true)
    public List<String> findAllByGuardsmanName(String guardsmanName) {
        List<DepartmentMember> guardsmen = departmentMemberRepository.findAllByGuardsmanName(guardsmanName);
        return guardsmen.stream()
                .map(guardsman -> guardsman.getGuardsman().getName())
                .toList();
    }


}
