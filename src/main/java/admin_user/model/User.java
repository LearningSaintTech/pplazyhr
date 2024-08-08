package admin_user.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import admin_user.dto.Clocking;
import admin_user.dto.EmployeeDetail;
import admin_user.dto.TicketDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;

	@OneToMany(mappedBy = "user")
	private Set<Clocking> clockings;

	private String password;
	private String role = "USER";
	private String fullname;

	@OneToMany(mappedBy = "user")
	private Set<TicketDto> tickets;

	private String token;

	private LocalDateTime resetTokenExpiryDate;

	/* private String employeeStatus; */

	private boolean active;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
	private EmployeeDetail employeeDetail;

	public EmployeeDetail getEmployeeDetail() {
		return employeeDetail;
	}

	public void setEmployeeDetail(EmployeeDetail employeeDetail) {
		this.employeeDetail = employeeDetail;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User() {
		super();
	}

	public User(String email, String password, String role, String fullname) {

		this.email = email;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getResetTokenExpiryDate() {
		return resetTokenExpiryDate;
	}

	public void setResetTokenExpiryDate(LocalDateTime resetTokenExpiryDate) {
		this.resetTokenExpiryDate = resetTokenExpiryDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role + ", fullname="
				+ fullname + ", active=" + active + ", employeeDetail=" + employeeDetail + "]";
	}

}