package com.workspace.workspace.service;

import com.workspace.workspace.dao.EmployeeRepository;
import com.workspace.workspace.dao.TeamMemberRepository;
import com.workspace.workspace.dao.TeamRepository;
import com.workspace.workspace.dto.AddTeamMemberRequest;
import com.workspace.workspace.dto.TeamCreateRequest;
import com.workspace.workspace.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService{

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Transactional
    @Override
    public Team createTeam(TeamCreateRequest request, String creatorEmail) {
        //find creator
        Employee creator=employeeRepository.findEmployeeByEmail(creatorEmail)
                .orElseThrow(()->new RuntimeException("Employee Not found with this email"));

        // Check if the creator is not employee
        if(creator.getRole()!= Role.ADMIN && creator.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("Team can only be created by admins and super admin.");
        }

        // If the creator is admin check that creator belongs to same department as the team
        if(creator.getRole()==Role.ADMIN) {
            request.setDepartment(creator.getDepartment());
        }

        if(creator.getRole()==Role.SUPER_ADMIN && request.getDepartment()==null) {
            throw new RuntimeException("Department is required");
        }

        //create team
        Team team=new Team();
        team.setCreatedDate(LocalDate.now());
        team.setTeamName(request.getTeamName());
        team.setDepartment(request.getDepartment());
        team.setStatus(Status.ACTIVE);
        team.setDescription(request.getDescription());
        teamRepository.save(team);

        team.setTeamId("TEAM"+team.getId());

//        teamRepository.save(team);

        return team;
    }

    @Override
    public List<Team> getAllTeams(String viewerEmail) {
        Employee viewer=employeeRepository.findEmployeeByEmail(viewerEmail)
                .orElseThrow(()->new RuntimeException("Employee with this email does not exist"));

        if(viewer.getRole()==Role.SUPER_ADMIN) {
            return teamRepository.findAll();
        }

        if(viewer.getRole()==Role.ADMIN) {
            return teamRepository.findTeamsByDepartment(viewer.getDepartment());
        }

        throw new RuntimeException(viewer.getRole()+"You are not authorized to view the team list.");
    }

    @Override
    public Team getTeamByTeamId(String teamId, String viewerEmail) {

        Team team=teamRepository.findTeamByTeamId(teamId)
                .orElseThrow(()->new RuntimeException("Team with this team id not found"));

        Employee viewer=employeeRepository.findEmployeeByEmail(viewerEmail)
                .orElseThrow(()-> new RuntimeException("Employee with this email not found"));

        if(viewer.getRole()!=Role.ADMIN && viewer.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("You are not authorized to view this team");
        }

        if(viewer.getRole()==Role.ADMIN && viewer.getDepartment()!=team.getDepartment()) {
            throw new RuntimeException("Admins can only view teams from their own department.");
        }

        return team;
    }

    @Override
    public List<TeamMember> getTeamMembers(String teamId) {
        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->new RuntimeException("Team not found"));

        return teamMemberRepository.findTeamMembersByTeam(team);
    }

    @Override
    public TeamMember addTeamMember(String teamId, AddTeamMemberRequest request, String creatorEmail) {
        Employee creator=employeeRepository
                .findEmployeeByEmail(creatorEmail)
                .orElseThrow(()->
                        new RuntimeException("Creator not found"));

        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->
                        new RuntimeException("Team not found"));

        Employee employee=employeeRepository
                .findEmployeeByEmployeeId(request.getEmployeeId())
                .orElseThrow(()->
                        new RuntimeException("Employee not found"));

        if(creator.getRole()!=Role.ADMIN && creator.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("You are not authorized to add team members");
        }

        if(creator.getRole()==Role.ADMIN && creator.getDepartment()!=team.getDepartment()) {
            throw new RuntimeException("Admin can only manage teams from their own department");
        }

        if(employee.getDepartment()!=team.getDepartment()) {
            throw new RuntimeException("Employee must belong to the same department as the team");
        }

        if(employee.getStatus()!=Status.ACTIVE) {
            throw new RuntimeException("Inactive employee cannot be added to a team");
        }

        if(teamMemberRepository.existsByTeamAndEmployee(team,employee)) {
            throw new RuntimeException("Employee is already a member of this team");
        }

        if(request.getTeamMemberRole()==TeamMemberRole.TEAM_LEAD &&
        teamMemberRepository.existsByTeamAndTeamMemberRoleAndStatus(
                team,
                TeamMemberRole.TEAM_LEAD,
                Status.ACTIVE
        )) {
            throw new RuntimeException("Team already has an active team lead");
        }

        TeamMember teamMember=new TeamMember();

        teamMember.setTeam(team);
        teamMember.setEmployee(employee);
        teamMember.setTeamMemberRole(request.getTeamMemberRole());
        teamMember.setStatus(Status.ACTIVE);
        teamMember.setJoinedDate(LocalDate.now());

        return teamMemberRepository.save(teamMember);
    }

    @Override
    public void removeTeamMember(String teamId, String employeeId, String removerEmail) {
        Employee remover=employeeRepository
                .findEmployeeByEmail(removerEmail)
                .orElseThrow(()->
                        new RuntimeException("Remover not found"));

        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->
                        new RuntimeException("Team not found"));

        Employee employee=employeeRepository
                .findEmployeeByEmployeeId(employeeId)
                .orElseThrow(()->
                        new RuntimeException("Employee not found"));

        if(remover.getRole()!=Role.ADMIN && remover.getRole()!=Role.SUPER_ADMIN) {
            throw new RuntimeException("You are not authorized to remove team members");
        }

        if(remover.getRole()!=Role.ADMIN && remover.getDepartment()!=team.getDepartment()) {
            throw new RuntimeException("Admin can only manage teams from their own department");
        }

        TeamMember teamMember=teamMemberRepository
                .findByTeamAndEmployee(team,employee)
                .orElseThrow(()->
                        new RuntimeException("Employee is not a member of this team"));

        if(teamMember.getStatus()==Status.INACTIVE) {
            throw new RuntimeException(
                    "Team member is already inactive"
            );
        }

        teamMember.setStatus(Status.INACTIVE);
        teamMemberRepository.save(teamMember);
    }

    @Override
    public List<TeamMember> getActiveTeamMembers(String teamId) {

        Team team=teamRepository
                .findTeamByTeamId(teamId)
                .orElseThrow(()->
                        new RuntimeException("Team not found"));

        return teamMemberRepository.findTeamMembersByTeamAndStatus(
                team,
                Status.ACTIVE
        );
    }
}
