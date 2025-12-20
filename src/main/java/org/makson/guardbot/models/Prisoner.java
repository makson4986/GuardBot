package org.makson.guardbot.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prisoners")
public class Prisoner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Builder.Default
    private LocalDateTime conclusionDate = LocalDateTime.now();
    private LocalDateTime releaseDate;
    private Integer prisonCell;
    private String reason;
}
