package com.workspace.workspace.service;

import com.workspace.workspace.dao.EmployeeRepository;
import com.workspace.workspace.dao.ProjectRepository;
import com.workspace.workspace.dao.TeamRepository;
import com.workspace.workspace.dto.ProjectCreateRequest;
import com.workspace.workspace.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private  EmployeeRepository employeeRepository;

    @Autowired
    private  TeamRepository teamRepository;

    @Autowired
    private  ProjectRepository projectRepository;


    @Transactional
    @Override
    public Project createProject(String teamId, ProjectCreateRequest request, String creatorEmail) {
        Employee creator=employeeRepository.findEmployeeByEmail(creatorEmail)
                .orElseThrow(()->new RuntimeException("this email does not exist"));

        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->
                        new RuntimeException("Employee not found with this email"));

        if(creator.getRole()!= Role.SUPER_ADMIN &&
        creator.getRole()!=Role.ADMIN) {
            throw new RuntimeException("Only admin and super admin can create projects");
        }

        if(creator.getRole()==Role.ADMIN && creator.getDepartment()!=team.getDepartment()) {
            throw new RuntimeException("Admin can create projects only for teams in their own department");
        }

        if(request.getStartDate()!=null &&
        request.getDueDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("Due date cannot be before start date");
        }

        Project project=new Project();

        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setTeam(team);
        project.setStartDate(request.getStartDate());
        project.setDueDate(request.getDueDate());
        project.setCreatedDate(LocalDate.now());

        if(request.getStartDate()==null) {
            project.setProjectStatus(ProjectStatus.PLANNED);
        } else{
            project.setProjectStatus(
                    ProjectStatus.IN_PROGRESS
            );
        }

        projectRepository.save(project);

        project.setProjectId(
                "PROJECT"+project.getId()
        );

        return project;
    }

    @Override
    public List<Project> getProjectsByTeam(String teamId) {

        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->
                        new RuntimeException("Team not found"));

        return projectRepository.findProjectsByTeam(team);
    }

    @Override
    public Project getProjectByProjectId(String projectId, String viewerEmail) {

        Employee viewer=employeeRepository.findEmployeeByEmail(viewerEmail)
                .orElseThrow(()->
                        new RuntimeException("Employee not found"));

        Project project=projectRepository
                .findProjectByProjectId(projectId);

        if(viewer.getRole()!=Role.ADMIN && viewer.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("You are not authorized to view this project");
        }

        if(viewer.getRole()==Role.ADMIN && viewer.getDepartment()!=project.getTeam().getDepartment()) {
            throw new RuntimeException("Admin can only view projects from their own department");
        }

        return  project;
    }
}
