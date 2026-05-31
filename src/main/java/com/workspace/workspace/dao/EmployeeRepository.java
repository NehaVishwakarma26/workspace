package com.workspace.workspace.dao;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findEmployeeByEmail(String email);

    Optional<Employee> findEmployeeByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    List<Employee> findEmployeesByDepartment(Department department);

    List<Employee> findEmployeesByRole(Role role);

    List<Employee> findEmployeesByRoleAndDepartment(Role role,Department department);

}
