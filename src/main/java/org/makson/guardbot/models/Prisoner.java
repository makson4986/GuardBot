package org.makson.guardbot.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prisoners")
public class Prisoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Builder.Default
    private LocalDate conclusionDate = LocalDate.now();
    private LocalDate releaseDate;
    private Integer prisonCell;
    private String reason;
}
