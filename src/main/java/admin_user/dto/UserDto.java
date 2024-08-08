package admin_user.dto;

import java.util.Set;

import admin_user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private boolean active;
    
//    @OneToMany(mappedBy = "user")
//    private Set<Clocking> clockings;
    
    @Pattern(regexp = "^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&+=])(?=\\S+$).{8,20}$", 
            message = "Password must be 8-20 characters long, contain at least one digit, one uppercase letter, one lowercase letter, one special character, and have no whitespace")
    private String password;
    private String role = "USER";
    private String fullname;
    
    private String token;

   
    
    @OneToMany(mappedBy = "user")
    private Set<ReimbursementDetails> reimbursementDetails; 
    
    @OneToMany(mappedBy = "user")
    private Set<TicketDto> tickets;
    

	public UserDto() {
        // Default constructor required by JPA
    }

    public UserDto(String email, String password, String role, String fullname) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
    }

    public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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


	
}