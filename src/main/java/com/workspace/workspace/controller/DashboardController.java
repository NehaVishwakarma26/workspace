package com.workspace.workspace.controller;

import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import com.workspace.workspace.service.DashboardServiceImpl;
import com.workspace.workspace.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardServiceImpl dashboardServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model
    ) {

        Employee employee=employeeServiceImpl
                .getEmployeeByEmail(authentication.getName())
                .orElseThrow(()->
                        new RuntimeException("Employee not found"));

        if(employee.getRole()== Role.SUPER_ADMIN) {
            dashboardServiceImpl
                    .loadSuperAdminDashboard(model);
            return "dashboard/super-admin-dashboard";
        }

        if(employee.getRole()==Role.ADMIN) {
            dashboardServiceImpl
                    .loadAdminDashboard(employee,model);
            return "dashboard/admin-dashboard";
        }

        dashboardServiceImpl.loadEmployeeDashboard(employee,model);

        return "dashboard/employee-dashboard";
    }

}
