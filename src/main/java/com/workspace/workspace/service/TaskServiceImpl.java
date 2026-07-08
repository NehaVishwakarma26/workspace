package com.workspace.workspace.service;

import com.workspace.workspace.dao.*;
import com.workspace.workspace.dto.TaskCreateRequest;
import com.workspace.workspace.dto.TaskDetailsResponse;
import com.workspace.workspace.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Task createTask(String projectId, TaskCreateRequest request, String creatorEmail) {

        Employee creator=employeeRepository.findEmployeeByEmail(creatorEmail)
                .orElseThrow(()-> new RuntimeException("Employee with this email does not exist"));

        Project project=projectRepository
                .findProjectByProjectId(projectId);

        if(creator.getRole()!= Role.ADMIN && creator.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("Only Admin and super admin can create tasks");
        }

        if(creator.getRole()==Role.ADMIN &&
        creator.getDepartment()!=project.getTeam().getDepartment()) {
            throw new RuntimeException("Admin can create tasks only for projects in their own department");
        }

        if(request.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Task due date cannot be in the past");
        }

        if(request.getEmployeeIds()==null || request.getEmployeeIds().isEmpty()) {
            throw new RuntimeException("At least one employee must be assigned");
        }

        Task task=new Task();

        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        task.setProject(project);
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDate.now());
        task.setTaskStatus(TaskStatus.TODO);

        taskRepository.save(task);

        task.setTaskId("TASK"+task.getId());
        taskRepository.save(task);

        for(String employeeId:request.getEmployeeIds()) {
            Employee employee=employeeRepository
                    .findEmployeeByEmployeeId(employeeId)
                    .orElseThrow(()->
                            new RuntimeException("employeee not found"));

            if(employee.getStatus()!=Status.ACTIVE) {
                throw new RuntimeException("Inactive employee cannot be assigned to task");
            }

            TeamMember teamMember=teamMemberRepository
                    .findByTeamAndEmployee(
                            project.getTeam(),
                            employee
                    )
                    .orElseThrow(()->
                            new RuntimeException(employee.getEmployeeId()+" is not a member of this project's team"));

            if(teamMember.getStatus()!=Status.ACTIVE) {
                throw new RuntimeException(
                        employee.getEmployeeId()
                        +" is not an active member of this project's team"
                );

            }
            TaskAssignment assignment=new TaskAssignment();

            assignment.setTask(task);
            assignment.setEmployee(employee);
            assignment.setAssignedDate(LocalDate.now());

            taskAssignmentRepository.save(assignment);
        }
        return task;
    }

    @Override
    public List<Task> getTasksByProject(String projectId) {

        Project project=projectRepository
                .findProjectByProjectId(projectId);

        return taskRepository.findTasksByProject(project);

    }

    @Override
    public List<TaskDetailsResponse> getTaskDetailsByProject(
            String projectId) {

        Project project = projectRepository
                .findProjectByProjectId(projectId);

        List<Task> tasks =
                taskRepository.findTasksByProject(project);

        List<TaskDetailsResponse> responses =
                new ArrayList<>();

        for (Task task : tasks) {

            List<TaskAssignment> assignments =
                    taskAssignmentRepository
                            .findTaskAssignmentsByTask(task);

            List<String> assignedEmployees =
                    new ArrayList<>();

            for (TaskAssignment assignment : assignments) {

                Employee employee =
                        assignment.getEmployee();

                assignedEmployees.add(
                        employee.getEmployeeId()
                                + " - "
                                + employee.getFirstName()
                                + " "
                                + employee.getLastName()
                );
            }

            TaskDetailsResponse response =
                    new TaskDetailsResponse();

            response.setTaskId(task.getTaskId());
            response.setTaskName(task.getTaskName());
            response.setDescription(task.getDescription());
            response.setDueDate(task.getDueDate());
            response.setCreatedDate(task.getCreatedAt());
            response.setTaskStatus(task.getTaskStatus());
            response.setAssignedEmployee(
                    assignedEmployees
            );

            responses.add(response);
        }

        return responses;
    }
}
