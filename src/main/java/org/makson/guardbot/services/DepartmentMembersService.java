package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.department.DepartmentMemberCreatingDto;
import org.makson.guardbot.dto.department.DepartmentMemberResponseDto;
import org.makson.guardbot.exceptions.DepartmentMemberAlreadyExistsException;
import org.makson.guardbot.exceptions.DepartmentNotFoundException;
import org.makson.guardbot.exceptions.GuardsmanNotFoundException;
import org.makson.guardbot.mappers.DepartmentMemberMapper;
import org.makson.guardbot.models.Department;
import org.makson.guardbot.models.DepartmentMember;
import org.makson.guardbot.models.Guardsman;
import org.makson.guardbot.repositories.DepartmentMemberRepository;
import org.makson.guardbot.repositories.DepartmentRepository;
import org.makson.guardbot.repositories.GuardsmanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentMembersService {
    private final DepartmentMemberRepository departmentMemberRepository;
    private final DepartmentRepository departmentRepository;
    private final GuardsmanRepository guardsmanRepository;
    private final DepartmentMemberMapper mapper;

    @Transactional(readOnly = true)
    public List<DepartmentMemberResponseDto> findAllByDepartmentName(String departmentName) {
        List<DepartmentMember> members = departmentMemberRepository.findAllByDepartmentName(departmentName);
        return mapper.mapDepartmentMember(members);
    }

    @Transactional(readOnly = true)
    public List<String> findAllByGuardsmanName(String guardsmanName) {
        List<DepartmentMember> guardsmen = departmentMemberRepository.findAllByGuardsmanName(guardsmanName);
        return guardsmen.stream()
                .map(guardsman-> guardsman.getDepartment().getName())
                .toList();
    }

    @Transactional()
    public void addMemberToDepartment(DepartmentMemberCreatingDto dto) {
        Optional<Guardsman> maybeGuardsman = guardsmanRepository.findByName(dto.guardsmanName());
        Optional<Department> maybeDepartment = departmentRepository.findDepartmentByName(dto.departmentName());

        if (maybeGuardsman.isEmpty()) {
            throw new GuardsmanNotFoundException("Guardsman not found");
        }

        if (maybeDepartment.isEmpty()) {
            throw new DepartmentNotFoundException("Department not found");
        }

        if (isExists(dto.guardsmanName(), dto.departmentName())) {
            throw new DepartmentMemberAlreadyExistsException("The guard has already been added to the department");
        }

        DepartmentMember member = DepartmentMember.builder()
                .guardsman(maybeGuardsman.get())
                .department(maybeDepartment.get())
                .role(dto.role())
                .build();

        departmentMemberRepository.save(member);
    }

    @Transactional()
    public void deleteMemberFromDepartment(String guardsmanName, String departmentName) {
        departmentMemberRepository.deleteByGuardsmanByDepartment(guardsmanName, departmentName);
    }

    private boolean isExists(String guardsman, String department) {
        Optional<DepartmentMember> member = departmentMemberRepository.findByGuardsmanByDepartment(guardsman, department);
        return member.isPresent();
    }
}
