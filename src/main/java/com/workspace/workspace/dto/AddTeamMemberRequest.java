package com.workspace.workspace.dto;

import com.workspace.workspace.model.TeamMemberRole;
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
