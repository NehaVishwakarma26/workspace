package com.workspace.workspace.service;

import com.workspace.workspace.dto.ProjectCreateRequest;
import com.workspace.workspace.model.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(
            String teamId,
            ProjectCreateRequest request,
            String creatorEmail
    );

    List<Project> getProjectsByTeam(String teamId);

    Project getProjectByProjectId(String projectId,String viewerEmail);
}
