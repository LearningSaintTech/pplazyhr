package admin_user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import admin_user.dto.EmployeeDetail;
import admin_user.dto.UserDto;
import admin_user.exception.EmailAlreadyExistsException;
import admin_user.exception.InvalidPasswordException;
import admin_user.model.User;
import admin_user.repositories.EmployeeRepository;
import admin_user.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final String PASSWORD_PATTERN = "^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&+=])(?=\\S+$).{8,20}$";
	private Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	@Override
	public User save(UserDto userDto) {
		
		if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered.");
        }
		
		//validatePassword(userDto.getPassword());
		User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()) , userDto.getRole(), userDto.getFullname());
		
		EmployeeDetail newEmployee = new EmployeeDetail();
        newEmployee.setFullName(user.getFullname());
        newEmployee.setEmail(user.getEmail());
        // set other fields as needed
//        newEmployee.setUser(user);
//        employeeRepository.save(newEmployee);
//        user.setEmployeeDetail(newEmployee);
//		return userRepository.save(user);
        
        user.setEmployeeDetail(newEmployee);
        newEmployee.setUser(user);
        System.out.println("User before save: " + user);
        System.out.println("EmployeeDetail before save: " + newEmployee);

        User savedUser = userRepository.save(user);

        System.out.println("Saved User: " + savedUser);

        return savedUser;
	}
	
	private void validatePassword(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new InvalidPasswordException("Password must be 8-20 characters long, contain at least one digit, one uppercase letter, one lowercase letter, one special character, and have no whitespace");
        }
    }
	
	public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
	
	public List<User> getAllUser() {
        return userRepository.findAll();
    }
	
	@Override
    public void updateUserRole(Long userId, String role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(role);
            userRepository.save(user);
        } else {
            // Handle user not found
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }
	
	public void updateUserPassword(Long userId, String newPassword) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
	
	public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
	
	public String generatePasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        System.out.println(">>>>>>>>>>"+user);
        user.setToken(token);
        user.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(5));
        // Save token to user or a separate table (depends on your implementation)
        // For simplicity, we just return the token here
        emailService.sendPasswordResetEmail(user.getEmail(), token);
        userRepository.save(user);
        return token;
    }
	
private User findUserByToken(String token) {
		
		System.out.println("ghirhgerihgiaer="+token);
		// TODO Auto-generated method stub
		User optionalUser = userRepository.findByToken(token);
		return optionalUser;
	}

	public void activateUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActive(true);
		userRepository.save(user);
	}

	public void deactivateUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActive(false);
		userRepository.save(user);
	}

	public boolean isUserActive(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return user.isActive();
	}
	
	public EmployeeDetail getEmployeeDetailByUserId(Long userId) {
		System.out.println("getting employee id >>"+userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        System.out.println(">>///////"+user);
        return user.getEmployeeDetail();
    }

}