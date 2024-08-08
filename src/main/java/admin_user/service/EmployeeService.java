package admin_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.dto.EmployeeDetail;
import admin_user.model.User;
import admin_user.repositories.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    

    public EmployeeDetail getEmployeeDetail(Long empId) {
    	System.out.println("printing only one user in controller>>>>>>");
        return employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
    }
    
    public List<EmployeeDetail> getEmployeeByEmpId(String empId) {
    	System.out.println("getting in service class");
        return employeeRepository.findByEmpId(empId);
    }

    public List<EmployeeDetail> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void updateEmployee(Long employeeId, EmployeeDetail employeeDetail) {

    	Optional<EmployeeDetail> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            EmployeeDetail existingEmployee = optionalEmployee.get();

            // Update the existing employee's details with the new employeeDetail
            existingEmployee.setEmpId(employeeDetail.getEmpId());
            existingEmployee.setFullName(employeeDetail.getFullName());
            existingEmployee.setDob(employeeDetail.getDob());
            existingEmployee.setGender(employeeDetail.getGender());
            existingEmployee.setBloodGroup(employeeDetail.getBloodGroup());
            existingEmployee.setPhno(employeeDetail.getPhno());
            existingEmployee.setAddress(employeeDetail.getAddress());
            existingEmployee.setDepartment(employeeDetail.getDepartment());
            existingEmployee.setEmail(employeeDetail.getEmail());
            existingEmployee.setDateOfJoining(employeeDetail.getDateOfJoining());
            existingEmployee.setSalary(employeeDetail.getSalary());
            existingEmployee.setShift(employeeDetail.getShift());

            // Save the updated employee to the database
            employeeRepository.save(existingEmployee);
        } else {
            System.out.println("not success");
        }
    }
    public void addEmployee(EmployeeDetail employeeDetail) {
        employeeRepository.save(employeeDetail);
    }
    
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

}

