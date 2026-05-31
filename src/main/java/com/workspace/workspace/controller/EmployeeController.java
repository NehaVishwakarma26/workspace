package com.workspace.workspace.controller;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import com.workspace.workspace.service.EmployeeService;
import com.workspace.workspace.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @GetMapping("/new")
    public String createEmployee(Model model) {
        Employee employee=new Employee();
        model.addAttribute("employee",employee);
        model.addAttribute("roles", Role.values());
        model.addAttribute("departments", Department.values());

        return "employee/employee-form";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        System.out.println("ROLE = " + employee.getRole());
        System.out.println("DEPT = " + employee.getDepartment());

        employeeServiceImpl.createEmployee(employee);

        return "redirect:/employee/list";
    }

    @GetMapping("/list")
    public String getEmployeeList(@RequestParam(required = false) Department department,@RequestParam(required = false) Role role,Model model) {


        List<Employee> employees=employeeServiceImpl.getAllEmployees(department,role);
        model.addAttribute("employees",employees);
        model.addAttribute("roles",Role.values());
        model.addAttribute("departments",Department.values());
        return "employee/employees";
    }

    @GetMapping("/detail/{employeeId}")
    public String getEmployeeDetails(@PathVariable String employeeId,Model model) {
        Employee employee=employeeServiceImpl.getEmployeeByEmployeeId(employeeId);

        model.addAttribute("employee",employee);

        return "employee/employee-detail";

    }

    @GetMapping("/update/{employeeId}")
    public String updateEmployeeDetails(@PathVariable String employeeId,Model model) {
        Employee employee=employeeServiceImpl.getEmployeeByEmployeeId(employeeId);

        model.addAttribute("employee",employee);

        return "employee/update-employee";
    }

    @PostMapping("/saveUpdatedEmployee/{employeeId}")
    public String saveUpdatedEmployee(@ModelAttribute("employee") Employee updatedEmployee,@PathVariable String employeeId) {
        employeeServiceImpl.updateEmployee(employeeId,updatedEmployee);
        return "redirect:/employee/list";
    }

    @GetMapping("/deactivate/{employeeId}")
    public String deactivateEmployee(@PathVariable String employeeId) {
        employeeServiceImpl.deactivateEmployee(employeeId);
        return "redirect:/employee/list";
    }


}
