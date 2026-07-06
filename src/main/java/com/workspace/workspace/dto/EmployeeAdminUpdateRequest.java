package com.workspace.workspace.dto;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Role;
import lombok.Data;

@Data
public class EmployeeAdminUpdateRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String designation;
    private Role role;
    private Department department;
    private String employeeId;
}
