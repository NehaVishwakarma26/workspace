package com.workspace.workspace.service;

import com.workspace.workspace.dao.EmployeeRepository;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Status;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {

        if(employeeRepository.existsByEmail(employee.getEmail()))
            throw new RuntimeException("Email already exists");

        if(employeeRepository.existsByEmployeeId(employee.getEmployeeId()))
            throw new RuntimeException("Employee ID already exists");

        employee.setJoiningDate(LocalDate.now());

        employee.setStatus(Status.ACTIVE);

        employeeRepository.save(employee);

        return employee;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {

        return employeeRepository.findEmployeeByEmployeeId(employeeId);
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(String employeeId, Employee updatedEmployee) {

            Employee employee=employeeRepository.findEmployeeByEmployeeId(employeeId)
                    .orElseThrow(()->new RuntimeException("Employee with this employee id does not exist"));

            employee.setEmail(updatedEmployee.getEmail());
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setPassword(updatedEmployee.getPassword());
            employee.setDesignation(updatedEmployee.getDesignation());
            employee.setDepartment(updatedEmployee.getDepartment());
            employee.setRole(updatedEmployee.getRole());

            employeeRepository.save(employee);

            return employee;

    }

    @Override
    public void deactivateEmployee(String employeeId) {

        Employee employee=employeeRepository.findEmployeeByEmployeeId(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));

        employee.setStatus(Status.INACTIVE);

    }
}
