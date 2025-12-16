package org.makson.guardbot.repositories;

import org.makson.guardbot.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
