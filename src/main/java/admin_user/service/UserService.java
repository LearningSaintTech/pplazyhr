package admin_user.service;

import java.util.List;
import java.util.Optional;

import admin_user.dto.EmployeeDetail;
import admin_user.dto.UserDto;
import admin_user.model.User;

public interface UserService {
	
	User save (UserDto userDto);

	Optional<User> getUserById(Long userId);
	
	List<User> getAllUser();
	void updateUserRole(Long userId, String role);

	void updateUserPassword(Long userId, String password);

	User findByEmail(String email);

	String generatePasswordResetToken(User user);

	void activateUser(Long userId);

	void deactivateUser(Long userId);

	EmployeeDetail getEmployeeDetailByUserId(Long userId);

}
