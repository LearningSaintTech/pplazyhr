package admin_user.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.dto.EmployeeDetail;

import java.util.List;




public interface EmployeeRepository extends JpaRepository<EmployeeDetail, Long> {

	List<EmployeeDetail> findByEmpId(String empId);

}