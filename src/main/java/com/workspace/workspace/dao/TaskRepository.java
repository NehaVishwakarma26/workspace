package com.workspace.workspace.dao;

import com.workspace.workspace.model.Project;
import com.workspace.workspace.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findTasksByProject(Project project);
}
