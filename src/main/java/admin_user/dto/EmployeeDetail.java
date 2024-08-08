package admin_user.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import admin_user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity(name="employeedetail")
public class EmployeeDetail {
		
		@Id
		@Column(name = "employeeId")
		@GeneratedValue(strategy=GenerationType.AUTO)
		Long employeeId;
		
		
		
		@Column(name = "dob")
		String dob;
		
		@Column(name = "emp_id")
	    private String empId;
		
		@Column(name = "gender")
		String gender;
		
		@Column(name = "bloodgroup")
		String bloodGroup;
		
		@Column(name = "phno")
		long phno;
		
		@Column(name = "address")
		String address;
		
		@Column(name = "department")
		String department;
		
		@Column(name = "email")
		String email;
		
		@Column(name = "fullname")
		String fullname;
		
		
		@Column(name = "dateofjoining")
		String dateOfJoining;
		
		@Column(name = "salary")
		long salary;

		String shift;
		
		@JsonBackReference
		@OneToOne(mappedBy = "employeeDetail")
		private User user;
		
		

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getEmpId() {
			return empId;
		}

		public void setEmpId(String empId) {
			this.empId = empId;
		}

		public String getShift() {
			return shift;
		}

		public void setShift(String shift) {
			this.shift = shift;
		}

		public Long getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Long employeeId) {
			this.employeeId = employeeId;
		}

		public String getDob() {
			return dob;
		}

		public void setDob(String dob) {
			this.dob = dob;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getBloodGroup() {
			return bloodGroup;
		}

		public void setBloodGroup(String bloodGroup) {
			this.bloodGroup = bloodGroup;
		}

		public long getPhno() {
			return phno;
		}

		public void setPhno(long phno) {
			this.phno = phno;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getFullName() {
			return fullname;
		}

		public void setFullName(String fullname) {
			this.fullname = fullname;
		}

		

		public String getDateOfJoining() {
			return dateOfJoining;
		}

		public void setDateOfJoining(String dateOfJoining) {
			this.dateOfJoining = dateOfJoining;
		}

		public long getSalary() {
			return salary;
		}

		public void setSalary(long salary) {
			this.salary = salary;
		}

		@Override
		public String toString() {
			return "EmployeeDetail [employeeId=" + employeeId + ", dob=" + dob + ", empId=" + empId + ", gender="
					+ gender + ", bloodGroup=" + bloodGroup + ", phno=" + phno + ", address=" + address
					+ ", department=" + department + ", email=" + email + ", fullname=" + fullname + ", dateOfJoining="
					+ dateOfJoining + ", salary=" + salary + ", shift=" + shift + "]";
		}
		
		

//		@Override
//		public String toString() {
//			return "EmployeeDetail [employeeId=" + employeeId + ", dob=" + dob + ", gender=" + gender + ", bloodGroup="
//					+ bloodGroup + ", phno=" + phno + ", address=" + address + ", department=" + department + ", email="
//					+ email + ", fullname=" + fullname + ", dateOfJoining=" + dateOfJoining + ", salary=" + salary
//					+ ", shift=" + shift +  "]";
//		}

		
		
		
}