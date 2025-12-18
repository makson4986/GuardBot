package org.makson.guardbot.repositories;

import org.makson.guardbot.models.DepartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
