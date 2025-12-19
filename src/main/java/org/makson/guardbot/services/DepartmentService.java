package org.makson.guardbot.services;

import lombok.RequiredArgsConstructor;
import org.makson.guardbot.dto.DepartmentInfoDto;
import org.makson.guardbot.dto.DepartmentMemberDto;
import org.makson.guardbot.exceptions.DepartmentNotFoundException;
import org.makson.guardbot.mappers.DepartmentMapper;
import org.makson.guardbot.models.Department;
import org.makson.guardbot.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMembersService departmentMembersService;
    private final DepartmentMapper mapper;

    public DepartmentInfoDto getDepartmentByName(String name) {
        Optional<Department> department = departmentRepository.findDepartmentByName(name);
        List<DepartmentMemberDto> departmentMembers = departmentMembersService.findAllByDepartmentName(name);

        if (department.isEmpty()) {
            throw new DepartmentNotFoundException("Department with name " + name + " not found");
        }

        return new DepartmentInfoDto(
                department.get().getId(),
                department.get().getName(),
                departmentMembers
        );
    }

    public List<String> findAllByGuardsmanName(String name) {
        return departmentMembersService.findAllByGuardsmanName(name);
    }
}
