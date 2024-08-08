package admin_user.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import admin_user.dto.Notification;
import admin_user.dto.TicketDto;
import admin_user.dto.UserDto;
import admin_user.model.User;
import admin_user.repositories.NotificationRepository;
import admin_user.repositories.TicketRepository;
import admin_user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

//	public TicketDto createTicket(TicketDto ticket) {
//		ticket.setStatus("Open");
//		ticket.setCreatedAt(LocalDateTime.now());
//		return ticketRepository.save(ticket);
//	}
    
    public TicketDto createTicket(TicketDto ticket, Long userId) {
        ticket.setStatus("Open");
        ticket.setCreatedAt(LocalDateTime.now());

        // Fetch the UserDto by userId from UserRepository
        User userDto = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User Dto is showing >>"+userDto);

        // Set the UserDto object in TicketDto
        ticket.setUser(userDto);

        return ticketRepository.save(ticket);
    }


    public List<TicketDto> getAllTickets() {
    	System.out.println(">>>>>>hello"+ticketRepository);
        return ticketRepository.findAll();
    }

//    public TicketDto updateTicketStatus(Long id, String status) {
//        TicketDto ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
//        ticket.setStatus(status);
//        return ticketRepository.save(ticket);
//    }
    
    
//    public TicketDto updateTicketStatus(Long id, String status) {
//        Optional<TicketDto> optionalTicket = ticketRepository.findById(id);
//        if (optionalTicket.isPresent()) {
//            TicketDto ticket = optionalTicket.get();
//            ticket.setStatus(status);
//            return ticketRepository.save(ticket);
//        }
//        return null;
//    }
    
    public TicketDto updateTicketStatus(Long id, String status, String userRole) {
        Optional<TicketDto> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            TicketDto ticket = optionalTicket.get();
            if ("Closed".equals(status) && !"ADMIN".equals(userRole)) {
                throw new RuntimeException("Only admins can close tickets.");
            }
            ticket.setStatus(status);
            ticket = ticketRepository.save(ticket);
         // Create a notification
            Notification notification = new Notification();
            notification.setUserId(ticket.getUser().getId());
            notification.setMessage("Your ticket status has changed to " + status);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
            return ticket;
        }
        return null;
    }


	public List<TicketDto> getAllTickets(String fullname) {
		// TODO Auto-generated method stub
		return ticketRepository.findAll();
	}
	
	public TicketDto getLastCreatedTicket(Long userId) {
        return ticketRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 1))
            .stream()
            .findFirst()
            .orElse(null);
    }
    
    
}
