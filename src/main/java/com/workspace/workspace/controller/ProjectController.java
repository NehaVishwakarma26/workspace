package com.workspace.workspace.controller;

import com.workspace.workspace.dto.ProjectCreateRequest;
import com.workspace.workspace.model.Project;
import com.workspace.workspace.model.Team;
import com.workspace.workspace.service.ProjectServiceImpl;
import com.workspace.workspace.service.TeamServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    private final ProjectServiceImpl projectServiceImpl;
    @Autowired
    private final TeamServiceImpl teamServiceImpl;

    @GetMapping("/team/{teamId}/new")
    public String createProject(
            @PathVariable String teamId,
            Authentication authentication,
            Model model
    ) {
        Team team=teamServiceImpl.getTeamByTeamId(teamId, authentication.getName());

        ProjectCreateRequest request=new ProjectCreateRequest();

        model.addAttribute("project",request);
        model.addAttribute("team",team);

        return "project/project-form";
    }

    @PostMapping("/team/{teamId}/save")
    public String saveProject(
            @PathVariable String teamId,
            @Valid @ModelAttribute("project") ProjectCreateRequest request,
            Authentication authentication
    ) {
        projectServiceImpl.createProject(
                teamId,
                request,
                authentication.getName()
        );

        return "redirect:/team/detail/"+teamId;
    }

    @GetMapping("/detail/{projectId}")
    public String getProjectDetails(
            @PathVariable String projectId,
            Authentication authentication,
            Model model) {

        Project project=projectServiceImpl.getProjectByProjectId(projectId, authentication.getName());
        model.addAttribute("project",project);
        return "project/project-detail";

    }
}
