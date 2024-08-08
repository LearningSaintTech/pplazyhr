package admin_user.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import admin_user.dto.EmployeeDetail;
import admin_user.dto.TicketDto;
import admin_user.dto.UserDto;
import admin_user.exception.EmailAlreadyExistsException;
import admin_user.exception.InvalidPasswordException;
import admin_user.model.User;
import admin_user.repositories.UserRepository;
import admin_user.service.CustomUserDetail;
import admin_user.service.TicketService;
import admin_user.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;

	private String successMessage;
	
	@Autowired
	private UserRepository userRepository;


	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
//	@GetMapping("/admin/tickets")
//    public String viewTickets(Model model) {
//		
//		System.out.println("Ticket  Genn>>>>>>>>>>>>>");
//        model.addAttribute("tickets", ticketService.getAllTickets());
//        return "Ticket";
//    }

/* show deshboard/profile templates of user */
@GetMapping("/emp/{userId}")
@PreAuthorize("hasRole('USER')")
public String viewEmpById(@PathVariable Long userId, Model model, Principal principal) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    EmployeeDetail employeeDetail = userService.getEmployeeDetailByUserId(userId);
    model.addAttribute("user", userDetails);
    model.addAttribute("employeeDetail", employeeDetail);
    return "deshboard";
}
	
    @GetMapping("/admin/tickets")
    public String viewTickets(Model model ) {
        // Get the UserDetails from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            
            // Assuming CustomUserDetail has a reference to User entity or relevant user details
            //List<TicketDto> tickets = ticketService.getTicketsForUser(userDetails.getFullname());
            List<TicketDto> tickets = ticketService.getAllTickets(userDetails.getFullname());
            logger.info("Fetched tickets: {}", tickets);
            model.addAttribute("user", userDetails.getFullname()); // Assuming userDetails has a getUser method
            model.addAttribute("tickets", tickets);
        } else {
            // Handle error case where principal is not an instance of UserDetails
        	logger.error("Principal is not an instance of UserDetails");
            return "error";
        }

        return "ticket";
    }
	
    @GetMapping("/user/createTicket")
    public String CreateTicket(Model model,Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		Long userId = ((CustomUserDetail) userDetails).getId(); // Adjsut this if your UserDetails implementation is different
        TicketDto lastTicket = ticketService.getLastCreatedTicket(userId);
        model.addAttribute("lastTicket", lastTicket);
        return "create-tickets";
    }
    
    //show attendance page 
	@GetMapping("/admin/clockings")
    public String viewClockings(Model model,Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		logger.info("User details: {}", userDetails);
		model.addAttribute("user", userDetails);
        return "attendence";
    }
	
	
	
	@GetMapping("/user/clockings")
    public String viewClockingsById(Model model ,Principal principal) {
		System.out.println("showing attendence page in attendance controller  >>>>>>>>>");
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
        return "attendance-by-id";
    }
	
    

	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}
	
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto,BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
            return "register";
        }
		try {
            userService.save(userDto);
            model.addAttribute("message", "Registered Successfully!");
        } catch (InvalidPasswordException e) {
        	logger.error("Invalid password: {}", e.getMessage());
            model.addAttribute("passwordError", e.getMessage());
            return "register";
        } catch (EmailAlreadyExistsException e) {
        	logger.error("Email already exists: {}", e.getMessage());
            model.addAttribute("emailError", e.getMessage());
            return "register";
        }
		return "login";
	}
	
	@GetMapping("/all/users")
	public String getAllUsers(Model model ,Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		List<User> users = userService.getAllUser();
		model.addAttribute("users", users);
		return "all-users";
	}
	
	@PostMapping("/update/{id}")
    public String updateUserRole(@PathVariable("id") Long userId, @ModelAttribute("user") UserDto userDto) {
        userService.updateUserRole(userId, userDto.getRole());
        return "redirect:/all/users"; // Redirect to the user list page after update
    }
	
	
	// Thymeleaf template for the password update form
	@GetMapping("/updatePassword/{id}")
    public String showUpdatePasswordForm(@PathVariable("id") Long userId, Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        if (userDetails == null) {
            // Handle the case where the user is not found
            return "redirect:/error";
        }
        model.addAttribute("user", userDetails);
        model.addAttribute("userId", userId);
        return "updatePasswordForm";
    }
	
	@PostMapping("/updatePassword/{id}")
    public String updatePassword(@PathVariable("id") Long userId, @ModelAttribute("user") UserDto userDto, Model model,RedirectAttributes redirectAttributes) {
        userService.updateUserPassword(userId, userDto.getPassword());
        Optional<User> updatedUser = userService.getUserById(userId);
        model.addAttribute("user", updatedUser);
        model.addAttribute("userId", userId);
        //model.addAttribute("successMessage", "Password updated successfully!"); 

        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully!");
        return "redirect:/updatePassword/" + userId;
    }
	
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password, or user account is disabled. please contact Admin");
            logger.error("Login error: Invalid username or password, or user account is disabled.");
        }
        return "login";
    }
	
//	@GetMapping("/{adminId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String getEmployeeProfile() {
//    	System.out.println("?????????????????????????????????");
//    	String currentUsername = getCurrentUsername();
//        EmployeeDetail employeeDetail = employeeService.getEmployeeDetail(userDto);
//        if (!employeeDetail.getFirstName().equals(currentUsername)) {
//            throw new AccessDeniedException("You are not authorized to view this profile");
//        }
//		
//		
//        return "aa";
//    }
	
	
	
	@GetMapping("user-page")
	public String userPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);		
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
	    String role = authorities.stream()
	                             .map(GrantedAuthority::getAuthority)
	                             .findFirst()
	                             .orElse("ROLE_USER");  // Default role if none found

	    // Add role to the model
	    model.addAttribute("role", role);
		return "user";
	}
	
	@GetMapping("admin-page")
	public String adminPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		System.out.println("modal"+model);
		return "admin";
	}
	
	@GetMapping("manager-page")
	public String managerPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "manager";
	}
	
	
	@GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }
	
	@PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "No account found with that email address.");
            logger.error("Forgot password error: No account found with email {}", email);
            return "forgot-password";
        }

      //send an email with the token
        String token = userService.generatePasswordResetToken(user);
        logger.error("Forgot password error: No account found with email {}", email);
        model.addAttribute("message", "A password reset link has been sent to " + email);
        return "forgot-password";
    }
	
	@GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Validate the token
        // If valid, show the form to reset the password
        model.addAttribute("token", token);
        return "reset-password";
    }
	
//	@PostMapping("/reset-password")
//    public String processResetPasswordForm(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
//        // Validate the token
//        // If valid, reset the password
//		System.out.println("reset password in postmappingm= "+token);
//
//        // Find the user by token (depends on your implementation)
//        User user = findUserByToken(token);
//        System.out.println("user token"+user);
//        if (user == null) {
//            model.addAttribute("error", "Invalid token.");
//            return "reset-password";
//        }
//
//        userService.updateUserPassword(user.getId(), password);
//        model.addAttribute("message", "Your password has been reset successfully.");
//        return "login";
//    }
	
	@PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("password") String password, Model model) {
        try {
        	User user = findUserByToken(token);
            userService.updateUserPassword(user.getId(), password);
            model.addAttribute("message", "Your password has been reset successfully.");
            return "login";
        } catch (TokenExpiredException e) {
        	logger.error("Token expired error: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        } catch (RuntimeException e) {
        	logger.error("Invalid token error: {}", e.getMessage());
            model.addAttribute("error", "Invalid token.");
            return "reset-password";
        }
    }
	
	

	private User findUserByToken(String token) {
		
		System.out.println("ghirhgerihgiaer="+token);
		// TODO Auto-generated method stub
		User optionalUser = userRepository.findByToken(token);
		if (optionalUser.getResetTokenExpiryDate().isAfter(LocalDateTime.now())) {
            return optionalUser;
        } else {
            throw new TokenExpiredException("Token has expired");
        }
	}
	
	
	public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
	
	//user active or inactive 
	@PostMapping("/activateUser")
	public String activateUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
		logger.info("Activating user ID: {}", userId);
		userService.activateUser(userId);
		redirectAttributes.addFlashAttribute("message", "User activated successfully");
		return "redirect:/all/users"; // Redirect to the user list or wherever you need
	}

	@PostMapping("/deactivateUser")
	public String deactivateUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
		logger.info("Deactivating user ID: {}", userId);
		userService.deactivateUser(userId);
		redirectAttributes.addFlashAttribute("message", "User deactivated successfully");
		return "redirect:/all/users"; // Redirect to the user list or wherever you need
	}

}
