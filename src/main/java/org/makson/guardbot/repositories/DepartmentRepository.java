package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findDepartmentByName(String name);

}
