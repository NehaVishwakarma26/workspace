package com.workspace.workspace.service;

import com.workspace.workspace.dao.*;
import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Status;
import com.workspace.workspace.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    @Override
    public void loadEmployeeDashboard(Employee employee, Model model) {

        var memberships=teamMemberRepository
                .findTeamMembersByEmployeeAndStatus(
                        employee,
                        Status.ACTIVE
                );

        var assignments=
                taskAssignmentRepository
                        .findTaskAssignmentsByEmployee(employee);

        long todoTasks=assignments.stream()
                .filter(assignment ->
                        assignment.getTask().getTaskStatus()== TaskStatus.TODO).count();

        long inProgressTasks=assignments.stream()
                .filter(assignment ->
                        assignment.getTask().getTaskStatus()==TaskStatus.IN_PROGRESS).count();

        long completedTasks=assignments.stream()
                .filter(assignment->
                        assignment.getTask().getTaskStatus()
                ==TaskStatus.COMPLETED).count();

        model.addAttribute("employee",employee);
        model.addAttribute("memberships",memberships);
        model.addAttribute("assignments",assignments);

        model.addAttribute("todoTasks",todoTasks);
        model.addAttribute("inProgressTasks",inProgressTasks);
        model.addAttribute("completedTasks",completedTasks);
    }

    @Override
    public void loadAdminDashboard(Employee admin,Model model) {

        Department department=admin.getDepartment();

        var employees=employeeRepository.findEmployeesByDepartment(department);

        var teams=teamRepository.findTeamsByDepartment(department);

        var projects=projectRepository.findProjectsByTeamDepartment(department);

        var tasks=taskRepository.findTasksByProjectTeamDepartment(department);

        long completedTasks=tasks.stream()
                .filter(task->
                        task.getTaskStatus()
                ==TaskStatus.COMPLETED).count();

        model.addAttribute("admin",admin);

        model.addAttribute("employeeCount",employees.size());

        model.addAttribute("teamCount",teams.size());

        model.addAttribute("projectCount",projects.size());

        model.addAttribute("taskCount",tasks.size());

        model.addAttribute("completedTaskCount",completedTasks);


    }


    @Override
    public void loadSuperAdminDashboard(Model model) {

        long employeeCount=employeeRepository.count();

        long teamCount= teamRepository.count();

        long projectCount=projectRepository.count();

        long taskCount=taskRepository.count();

        long completedTaskCount=taskRepository
                .countByTaskStatus(
                        TaskStatus.COMPLETED
                );

        model.addAttribute("employeeCount",employeeCount);
        model.addAttribute("teamCount",teamCount);
        model.addAttribute("projectCount",projectCount);
        model.addAttribute("taskCount",taskCount);
        model.addAttribute("completedTaskCount",completedTaskCount);
    }
}
