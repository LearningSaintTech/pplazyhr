package admin_user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import admin_user.dto.EmployeeDetail;
import admin_user.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
	UserDetailsService userDetailsService;

    @GetMapping("/{empId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String getEmployeeProfile(@RequestParam("id") Long id, Model model,Principal principal) {
    	System.out.println("?????????????????????????????????"+id);
        String currentUsername = getCurrentUsername();
        EmployeeDetail employeeDetail = employeeService.getEmployeeDetail(id);
        System.out.println("employeeDetail==="+employeeDetail);
        if (!employeeDetail.getFullName().equals(currentUsername)) {
            throw new AccessDeniedException("You are not authorized to view this profile");
        }
        return "deshboard";
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String getEmpById(@RequestParam("empId") String empId, Model model,Principal principal) {
        List<EmployeeDetail> employees = employeeService.getEmployeeByEmpId(empId);
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("employees", employees);
        model.addAttribute("user", userDetails);
        return "show-emp"; // Make sure this matches your Thymeleaf template name
    }
    
   
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String getAllEmployees(Model model,Principal principal) {
        List<EmployeeDetail> employees = employeeService.getAllEmployees();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        System.out.println(">>>>>>>>>>>>>>>>EMP "+employees);
        model.addAttribute("employees", employees);
        model.addAttribute("user", userDetails);
        //model.addAttribute("employees", employees);
        return "show-emp";
    }

    @PostMapping("/update/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateEmployee(@PathVariable Long employeeId,
            @ModelAttribute EmployeeDetail employeeDetail) {
        System.out.println("calledddddddddddddd>>>>>>>>>>>");
        employeeService.updateEmployee(employeeId, employeeDetail);
        System.out.println("changedddddd"+employeeId+ employeeDetail);
        return "redirect:/employee/all";
    }
    
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        //model is for form binding
        model.addAttribute("employeeDetail", new EmployeeDetail());
        return "show-emp"; //template name
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employeeDetail") EmployeeDetail employeeDetail) {
        System.out.println("Received employee data: "+employeeDetail);
         employeeService.addEmployee(employeeDetail);
         return "redirect:/employee/all";
    }
    
    @PostMapping("/delete/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String deleteEmployee(@PathVariable Long employeeId) {
        System.out.println("Employee deleted: " + employeeId);
        employeeService.deleteEmployee(employeeId);
        return "redirect:/employee/all";    
      }


    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
