package org.makson.guardbot.services;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import lombok.RequiredArgsConstructor;
import org.makson.guardbot.repositories.DepartmentRepository;

@BService
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;



}
