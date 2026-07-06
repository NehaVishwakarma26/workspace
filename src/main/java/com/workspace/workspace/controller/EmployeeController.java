package com.workspace.workspace.controller;

import com.workspace.workspace.dto.EmployeeAdminUpdateRequest;
import com.workspace.workspace.dto.EmployeeSelfUpdateRequest;
import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import com.workspace.workspace.service.EmployeeService;
import com.workspace.workspace.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
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
    public String saveEmployee(@ModelAttribute("employee") Employee employee,Authentication authentication) {
        System.out.println("ROLE = " + employee.getRole());
        System.out.println("DEPT = " + employee.getDepartment());

        employeeServiceImpl.createEmployee(employee,authentication.getName());

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

    @GetMapping("/me/update")
    public String selfUpdateEmployeeDetails(Authentication authentication,Model model) {
        String email=authentication.getName();
        Employee employee=employeeServiceImpl.getEmployeeByEmail(email)
                .orElseThrow(()->new RuntimeException("Employee with this email not found."));
        EmployeeSelfUpdateRequest request=new EmployeeSelfUpdateRequest();
        request.setFirstName(employee.getFirstName());
        request.setLastName(employee.getLastName());
//        request.setEmail(employee.getEmail());
        model.addAttribute("employee",request);

        return "employee/self-update-employee";
    }

    @PostMapping("/me/saveUpdatedEmployee")
    public String saveSelfUpdateEmployeeDetails(Authentication authentication,@ModelAttribute("employee") EmployeeSelfUpdateRequest updateRequest) {
        String email=authentication.getName();
        employeeServiceImpl.updateOwnProfile(email,updateRequest);
        return "redirect:/employee/me";
    }


    @GetMapping("/update/{employeeId}")
    public String updateEmployeeDetails(@PathVariable String employeeId, Model model) {
        Employee employee=employeeServiceImpl.getEmployeeByEmployeeId(employeeId);
        EmployeeAdminUpdateRequest request=new EmployeeAdminUpdateRequest();

        request.setFirstName(employee.getFirstName());
        request.setLastName(employee.getLastName());
        request.setDesignation(employee.getDesignation());
        request.setDepartment(employee.getDepartment());
        request.setRole(employee.getRole());
        request.setEmail(employee.getEmail());
        request.setEmployeeId(employee.getEmployeeId());

        model.addAttribute("employee",request);
        model.addAttribute("departments",Department.values());
        model.addAttribute("roles",Role.values());

        return "employee/update-employee";
    }

    @PostMapping("/saveUpdatedEmployee/{employeeId}")
    public String saveUpdatedEmployee(
            @ModelAttribute("employee")
            EmployeeAdminUpdateRequest updateRequest,
            @PathVariable String employeeId,
            Authentication authentication) {
        employeeServiceImpl.updateEmployeeByAdmin(employeeId,authentication.getName(),updateRequest);
        return "redirect:/employee/list";
    }


    @GetMapping("/deactivate/{employeeId}")
    public String deactivateEmployee(@PathVariable String employeeId) {
        employeeServiceImpl.deactivateEmployee(employeeId);
        return "redirect:/employee/list";
    }

    @GetMapping("/me")
    public String getEmployee(Authentication authentication,Model model) {
        String email=authentication.getName();
        Employee employee=employeeServiceImpl.getEmployeeByEmail(email)
                .orElseThrow(()->new RuntimeException("email not found"));
        model.addAttribute("employee",employee);
        return "employee/employee-detail";
    }


}
