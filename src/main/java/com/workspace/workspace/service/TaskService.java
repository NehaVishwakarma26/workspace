package com.workspace.workspace.service;

import com.workspace.workspace.dto.TaskCreateRequest;
import com.workspace.workspace.dto.TaskDetailsResponse;
import com.workspace.workspace.model.Task;
import com.workspace.workspace.model.TeamMember;

import java.util.List;

public interface TaskService {

    Task createTask(
            String projectId,
            TaskCreateRequest request,
            String creatorEmail
    );

    List<Task> getTasksByProject(String projectId);

    List<TaskDetailsResponse> getTaskDetailsByProject(String projectId);
}
