package com.workspace.workspace.dto;

import com.workspace.workspace.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamCreateRequest {
    @NotBlank
    private String teamName;

    @NotBlank
    private String description;

    @NotNull
    private Department department;
}
