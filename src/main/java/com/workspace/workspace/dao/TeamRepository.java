package com.workspace.workspace.dao;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long>{

    List<Team> findTeamsByDepartment(Department department);

    Optional<Team> findTeamByTeamId(String teamId);
}
