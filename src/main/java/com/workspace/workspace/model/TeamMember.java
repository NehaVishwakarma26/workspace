package com.workspace.workspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "team_members",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_team_employee",
                columnNames = {"team_id","employee_id"}
        )
})
@Data
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "employee_id",nullable = false)
    private Employee employee;

    @NotNull
    private TeamMemberRole teamMemberRole;

    @NotNull
    private LocalDate joinedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}
