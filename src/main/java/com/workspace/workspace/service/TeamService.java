package com.workspace.workspace.service;

import com.workspace.workspace.dto.TeamCreateRequest;
import com.workspace.workspace.dto.AddTeamMemberRequest;
import com.workspace.workspace.model.Team;
import com.workspace.workspace.model.TeamMember;

import java.util.List;

public interface TeamService {

    Team createTeam(TeamCreateRequest request,String creatorEmail);

    List<Team> getAllTeams(String viewerEmail);

    Team getTeamByTeamId(String teamId,String viewerEmail);

    List<TeamMember> getTeamMembers(String teamId);

    TeamMember addTeamMember(String teamId, AddTeamMemberRequest request,String creatorEmail);

    void removeTeamMember(String teamId,String employeeId,String removerEmail);
}
