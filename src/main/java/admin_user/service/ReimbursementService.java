package admin_user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import admin_user.dto.Notification;
import admin_user.dto.ReimbursementDetails;
import admin_user.dto.TicketDto;
import admin_user.model.User;
import admin_user.repositories.NotificationRepository;
import admin_user.repositories.ReimbursementRepository;
import admin_user.repositories.UserRepository;

@Service
public class ReimbursementService {

    @Autowired
    private ReimbursementRepository reimbursementRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;


    public ReimbursementDetails createFile(ReimbursementDetails details, Long userId) {
        details.setStatus("Open");
        details.setCreatedAt(LocalDateTime.now());

        // Fetch the UserDto by userId from UserRepository
        User userDto = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User Dto is showing >>" + userDto);

        // Set the UserDto object in TicketDto
        details.setUser(userDto);

        return reimbursementRepository.save(details);
    }
    
    public ReimbursementDetails updateFileStatus(Long id, String status, String userRole) {
    	System.out.println("helloooo   test in service for update ticket files");
        Optional<ReimbursementDetails> optionalFiles = reimbursementRepository.findById(id);
        if (optionalFiles.isPresent()) {
            ReimbursementDetails reimbursementDetails = optionalFiles.get();
            if ("Closed".equals(status) && !"ADMIN".equals(userRole)) {
                throw new RuntimeException("Only admins can close tickets.");
            }
            reimbursementDetails.setStatus(status);
            reimbursementDetails = reimbursementRepository.save(reimbursementDetails);
         // Create a notification
            Notification notification = new Notification();
            notification.setUserId(reimbursementDetails.getUser().getId());
            notification.setMessage("Your reimbursement status has changed to " + status);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
            return reimbursementDetails;
        }
        return null;
    }

    public List<ReimbursementDetails> getAllFiles(String fullname) {
        System.out.println("Fetching all reimbursements for user ID: ");
        return reimbursementRepository.findAll();
    }

    public ReimbursementDetails getLastCreatedFile(Long userId) {
        return reimbursementRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

	public Optional<ReimbursementDetails> findById(Long id) {
		// TODO Auto-generated method stub
		System.out.println("Ashihihiihihhhihihihihih");
		return reimbursementRepository.findById(id);
	}
}
