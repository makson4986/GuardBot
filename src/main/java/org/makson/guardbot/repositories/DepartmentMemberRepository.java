package org.makson.guardbot.repositories;

import org.makson.guardbot.models.DepartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Integer> {
    @Query("""
            select dep
            from DepartmentMember dep
            join dep.department d
            where d.name = :name
            """)
    List<DepartmentMember> findAllByDepartmentName(@Param("name") String name);


    @Query("""
            select dep
            from DepartmentMember dep
            join dep.guardsman g
            where  g.name =:name
            """)
    List<DepartmentMember> findAllByGuardsmanName(@Param("name") String guardsmanName);

    @Query("""
            select dep
            from DepartmentMember  dep
            where dep.guardsman.name = :guardsman and
            dep.department.name = :department
            """)
    Optional<DepartmentMember> findByGuardsmanByDepartment(@Param("guardsman") String guardsman, @Param("department") String department);


    @Modifying
    @Query("""
            delete from DepartmentMember dep
            where dep.guardsman.name = :guardsman and
            dep.department.name = :department
            """)
    void deleteByGuardsmanByDepartment(@Param("guardsman") String guardsman, @Param("department") String department);
}
