package admin_user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.dto.Notification;
import admin_user.repositories.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Other methods...

    public void markAllAsReadForUser(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }
    
   
}
