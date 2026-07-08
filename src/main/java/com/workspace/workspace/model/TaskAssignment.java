package com.workspace.workspace.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(
        name = "task_assignments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_task_employee",
                        columnNames = {
                                "task_id",
                                "employee_id"
                        }
                )
        }
)
@Data
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "task_id",
            nullable = false
    )
    private Task task;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;

    @NotNull
    private LocalDate assignedDate;
}