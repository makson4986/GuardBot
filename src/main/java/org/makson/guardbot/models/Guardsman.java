package org.makson.guardbot.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guardsmen")
public class Guardsman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id")
    private Rank rank;

    @Builder.Default
    private int points = 0;

    @Builder.Default
    private int specialReports = 0;

    private LocalDateTime lastReports;

    @Builder.Default
    @OneToMany(
            mappedBy = "guardsman",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DepartmentMember> departments = new ArrayList<>();

    public void addToDepartment(Department department, DepartmentRole role) {
        DepartmentMember departmentMember = DepartmentMember.builder()
                .guardsman(this)
                .department(department)
                .role(role)
                .build();
        departments.add(departmentMember);
    }
}
