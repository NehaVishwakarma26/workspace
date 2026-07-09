package com.workspace.workspace.dao;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Project;
import com.workspace.workspace.model.Task;
import com.workspace.workspace.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findTasksByProject(Project project);

    List<Task> findTasksByProjectTeamDepartment(Department department);

    long countByTaskStatus(TaskStatus taskStatus);
}
