package admin_user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import admin_user.dto.Notification;
import admin_user.repositories.NotificationRepository;
import admin_user.service.CustomUserDetail;
import admin_user.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private NotificationService notificationService;

    
    
    @GetMapping
    public List<Notification> getNotifications(Principal principal) {
    	System.out.println("aleeeerrrrrrrrrrrrtttttttttt");
        // Assuming the principal name is the username
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        Long userId = ((CustomUserDetail) userDetails).getId(); // Get user ID from user details
        System.out.println(notificationRepository.findByUserIdAndIsReadFalse(userId));
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }
    
    @PostMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead(Principal principal) {
    	System.out.println("aleerrrrttttt2222");
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        Long userId = ((CustomUserDetail) userDetails).getId(); // Get user ID from user details
        notificationService.markAllAsReadForUser(userId);
        return ResponseEntity.ok().build();
    }
}
