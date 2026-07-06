package com.workspace.workspace.dto;

import lombok.Data;

@Data
public class EmployeeSelfUpdateRequest {

    private String firstName;
    private String lastName;
    private String email;
}
