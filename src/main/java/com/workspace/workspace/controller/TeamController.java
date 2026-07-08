package com.workspace.workspace.controller;

import com.workspace.workspace.dto.AddTeamMemberRequest;
import com.workspace.workspace.dto.TeamCreateRequest;
import com.workspace.workspace.model.*;
import com.workspace.workspace.service.EmployeeServiceImpl;
import com.workspace.workspace.service.ProjectServiceImpl;
import com.workspace.workspace.service.TeamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    @Autowired
    private final TeamServiceImpl teamService;

    @Autowired
    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private final ProjectServiceImpl projectServiceImpl;

    @GetMapping("/new")
    public String createTeam(Model model) {

        TeamCreateRequest request=new TeamCreateRequest();

        model.addAttribute("team",request);
        model.addAttribute("departments", Department.values());
        return "team/team-form";

    }

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute TeamCreateRequest request, Authentication authentication) {
        String email=authentication.getName();

        teamService.createTeam(request,email);

        return "team/teams";
    }

    @GetMapping("/list")
    public String getTeamList(Authentication authentication,Model model) {
        List<Team> teams=teamService.getAllTeams(authentication.getName());
        model.addAttribute("teams",teams);
        return "/team/teams";
    }

    @GetMapping("/detail/{teamId}")
    public String getTeamDetails(@PathVariable String teamId,Authentication authentication,Model model) {
        Team team=teamService.getTeamByTeamId(
                teamId,
                authentication.getName()
        );

        List<TeamMember> members=teamService.getTeamMembers(teamId);
        List<Project> projects=projectServiceImpl.getProjectsByTeam(teamId);

        model.addAttribute("projects",projects);
        model.addAttribute("team",team);
        model.addAttribute("members",members);

        return "team/team-detail";
    }

    @GetMapping("/{teamId}/members/add")
    public String showAddMemberForm(@PathVariable String teamId,Authentication authentication,Model model) {
        Team team=teamService.getTeamByTeamId(teamId,authentication.getName());

        List<Employee> employees=employeeServiceImpl
                .getEmployeesByDepartment(team.getDepartment());

        AddTeamMemberRequest request=new AddTeamMemberRequest();

        model.addAttribute("team",team);
        model.addAttribute("member",request);
        model.addAttribute("employees",employees);
        model.addAttribute(
                "memberRoles",
                TeamMemberRole.values()
        );

        return "team/add-team-member";

    }

    @PostMapping("/{teamId}/members/add")
    public String addTeamMember(
            @PathVariable String teamId,
            @ModelAttribute("member") AddTeamMemberRequest request,
            Authentication authentication
    ) {
        teamService.addTeamMember(
                teamId,
                request,
                authentication.getName()
        );

        return "redirect:/team/detail/"+teamId;
    }

    @PostMapping("/{teamId}/members/{employeeId}/remove")
    public String removeTeamMember(
            @PathVariable String teamId,
            @PathVariable String employeeId,
            Authentication authentication
    ) {
        teamService.removeTeamMember(
                teamId,
                employeeId,
                authentication.getName()
        );

        return "redirect:/team/detail/"+teamId;
    }

}
