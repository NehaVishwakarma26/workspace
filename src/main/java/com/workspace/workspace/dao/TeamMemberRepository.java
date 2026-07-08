package com.workspace.workspace.dao;

import com.workspace.workspace.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember,String> {

    List<TeamMember> findTeamMembersByTeam(Team team);

    boolean existsByTeamAndEmployee(Team team, Employee employee);

    boolean existsByTeamAndTeamMemberRoleAndStatus(
            Team team,
            TeamMemberRole teamMemberRole,
            Status status
    );

    Optional<TeamMember> findByTeamAndEmployee(Team team, Employee employee);

}
