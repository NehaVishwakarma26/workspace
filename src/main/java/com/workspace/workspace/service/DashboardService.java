package com.workspace.workspace.service;

import com.workspace.workspace.model.Employee;
import org.springframework.ui.Model;

public interface DashboardService {

    void loadEmployeeDashboard(
            Employee employee,
            Model model
    );

    void loadAdminDashboard(
            Employee admin,
            Model model
    );

    void loadSuperAdminDashboard(
            Model model
    );
}
