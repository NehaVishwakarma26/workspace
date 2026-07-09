package com.workspace.workspace.dao;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Project;
import com.workspace.workspace.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findProjectsByTeam(Team team);

    Project findProjectByProjectId(String projectId);

    List<Project> findProjectsByTeamDepartment(Department department);

}
