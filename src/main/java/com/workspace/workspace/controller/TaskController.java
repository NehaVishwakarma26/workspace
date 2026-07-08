package com.workspace.workspace.controller;

import com.workspace.workspace.dto.TaskCreateRequest;
import com.workspace.workspace.model.Project;
import com.workspace.workspace.model.TeamMember;
import com.workspace.workspace.service.ProjectServiceImpl;
import com.workspace.workspace.service.TaskService;
import com.workspace.workspace.service.TaskServiceImpl;
import com.workspace.workspace.service.TeamServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskServiceImpl;
    private final ProjectServiceImpl projectServiceImpl;
    private final TeamServiceImpl teamServiceImpl;

    @GetMapping("/project/{projectId}/new")
    public String createTask(
            @PathVariable String projectId,
            Authentication authentication,
            Model model
    ) {

        Project project=
                projectServiceImpl.getProjectByProjectId(
                        projectId,
                        authentication.getName()
                );

        List<TeamMember> members=
                teamServiceImpl.getActiveTeamMembers(project.getTeam().getTeamId());

        TaskCreateRequest request=new TaskCreateRequest();

        model.addAttribute("task",request);
        model.addAttribute("project",project);
        model.addAttribute("members",members);

        return "task/task-form";
    }

    @PostMapping("/project/{projectId}/save")
    private String saveTask(
            @PathVariable String projectId,
            @Valid
            @ModelAttribute("task") TaskCreateRequest createRequest,
            Authentication authentication) {

        taskServiceImpl.createTask(
                projectId,
                createRequest,
                authentication.getName()
        );

        return "redirect:/project/detail/"+projectId;
    }
}
