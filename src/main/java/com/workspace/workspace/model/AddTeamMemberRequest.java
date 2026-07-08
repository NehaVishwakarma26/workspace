package com.workspace.workspace.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTeamMemberRequest {

    @NotBlank
    private String employeeId;

    @NotNull
    private TeamMemberRole teamMemberRole;
}
