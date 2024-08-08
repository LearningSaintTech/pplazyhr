package admin_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import admin_user.dto.TicketDto;
import admin_user.repositories.TicketRepository;
import admin_user.service.TicketService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
	UserDetailsService userDetailsService;
    
//    @PostMapping("/create")
//    public String createTicket(@ModelAttribute TicketDto ticket) {
//    	System.out.println("tickets controller is calling --------->");
//    	ticketService.createTicket(ticket);
//    	return "create-tickets";
//    }
    
    @PostMapping("/create")
    public String createTicket(@ModelAttribute("ticket") TicketDto ticket,Model model,Principal principal, @RequestParam Long userId) {
    	System.out.println("tickett createddd  .....");
    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		System.out.println("userDetails"+userDetails);
		model.addAttribute("user", userDetails);
    	ticketService.createTicket(ticket, userId);
    	return "create-tickets";
    }

//    @GetMapping("/all")
//    public List<TicketDto> getAllTickets() {
//    	System.out.println("shshshshshshshshssh");
//    	
//        return ticketService.getAllTickets();
//    }

//    @PutMapping("/update/{id}")
//    public TicketDto updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
//        return ticketService.updateTicketStatus(id, status);
//    }
//    
//    @PostMapping("/update-status")
//    public String updateTicketStatus(@RequestParam Long ticketId, @RequestParam String status) {
//    	System.out.println("Status is working perfectly >>>>>>>>>>>>");
//        ticketService.updateTicketStatus(ticketId, status);
//        return "redirect:/admin/tickets";
//    }
    @PostMapping("/update-status")
    public String updateTicketStatus(@RequestParam Long ticketId, @RequestParam String status, @RequestParam String userRole) {
        ticketService.updateTicketStatus(ticketId, status, userRole);
        return "redirect:/admin/tickets";
    }
}
