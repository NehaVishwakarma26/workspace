package com.workspace.workspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "employeeId cannot be blank")
    @Pattern(regexp = "^EMP\\d+$",message = "Employee ID should start with EMP")
    @Column(unique = true)
    private String employeeId;

    @Size(min = 2,max = 50,message = "First name length should be minimum 2 and maximum 50 characters")
    @NotBlank
    private String firstName;

    @NotBlank
    @Size(min = 2,max = 50,message = "Last name length should be minimum 2 and maximum 50 characters")
    private String lastName;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @Size(min = 8,message = "Password must be at least 8 characters long")
    @NotBlank
    private String password;

    @NotBlank
    private String designation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Department department;
}
