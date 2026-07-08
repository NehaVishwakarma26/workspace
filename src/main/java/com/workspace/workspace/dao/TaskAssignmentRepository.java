package com.workspace.workspace.dao;

import com.workspace.workspace.model.Task;
import com.workspace.workspace.model.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Long> {

    List<TaskAssignment> findTaskAssignmentsByTask(Task task);
}
