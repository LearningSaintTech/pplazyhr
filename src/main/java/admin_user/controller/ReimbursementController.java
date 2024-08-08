package admin_user.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import admin_user.dto.ReimbursementDetails;
import admin_user.repositories.ReimbursementRepository;
import admin_user.service.CustomUserDetail;
import admin_user.service.ReimbursementService;

@Controller
public class ReimbursementController {
	
	
	@Autowired
	private ReimbursementRepository reimbursementRepository;
	@Autowired
    private ReimbursementService reimbursementService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@GetMapping("/admin/reimbsFile")
    public String viewFiles(Model model) {
        // Get the UserDetails from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
        	System.out.println("hllllllllll333333333333");
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            String role = userDetails.getRoles();
            System.out.println("userDetailsuserDetailsuserDetails"+userDetails);
            // Assuming CustomUserDetail has a reference to User entity or relevant user details
            //List<TicketDto> tickets = ticketService.getTicketsForUser(userDetails.getFullname());
            List<ReimbursementDetails> reimbursementDetails = reimbursementService.getAllFiles(userDetails.getFullname());
            System.out.println("reimbursementDetailsreimbursementDetails"+reimbursementDetails);
            model.addAttribute("user", userDetails.getFullname()); // Assuming userDetails has a getUser method
            model.addAttribute("role", role);
            model.addAttribute("reimbursementDetails", reimbursementDetails);
        } else {
            // Handle error case where principal is not an instance of UserDetails
            return "error";
        }

        return "reimbursements-list";
    }
	
	@PostMapping("/update-files")
	public String updateTicketStatus(@RequestParam(required = false) Long reimbursementId, @RequestParam(required = false) String status, @RequestParam(required = false) String userRole) {
	    if (reimbursementId == null || status == null || userRole == null) {
	        return "error"; // Or some error page/message
	    }

	    reimbursementService.updateFileStatus(reimbursementId, status, userRole);
	    return "redirect:/admin/reimbsFile";
	}
	
	//get reimbursement form >>>>>>>>>>>>>>>>>>>> 
	@GetMapping("/createForm")
	public String createFile(Model model, Principal principal) {
	    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
	    System.out.println("userDetails====" + userDetails);

	    Long userId = ((CustomUserDetail) userDetails).getId(); // Adjust this if your UserDetails implementation is different
	    ReimbursementDetails reimbursementDetails = reimbursementService.getLastCreatedFile(userId);
	    
	    if (reimbursementDetails != null) {
	        model.addAttribute("reimbursement", reimbursementDetails);
	    } else {
	        model.addAttribute("reimbursement", new ReimbursementDetails());
	    }

	    model.addAttribute("user", userDetails);
	    
	 // List of categories to populate the dropdown
	    List<String> categories = Arrays.asList("Electronics", "Grocery", "Other");
	    model.addAttribute("categories", categories);
	    
	    return "reimbursement-form";
	}

	
//	@PostMapping("/create-reimbursement")
//    public String createReimbursementFile(@ModelAttribute("reimbursementDetails") ReimbursementDetails reimbursementDetails,Model model,Principal principal, @RequestParam Long userId) {
//    	System.out.println("reimbursementDetails createddd  ....."+reimbursementDetails);
//    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
//		System.out.println("userDetails"+userDetails);
//		model.addAttribute("user", userDetails);
//    	reimbursementService.createFile(reimbursementDetails, userId);
//    	return "redirect:/createForm";
//    }
	
	@PostMapping("/create-reimbursement")
	public String createReimbursementFile(
	        @ModelAttribute("reimbursementDetails") ReimbursementDetails reimbursementDetails,
	        @RequestParam("file") MultipartFile file,
	        Model model,
	        Principal principal,
	        @RequestParam Long userId) {
		System.out.println("Reimbursement Details created: " + reimbursementDetails);
	    System.out.println("Reimbursement files created: " + file);

	    // Set the file details
	    if (!file.isEmpty()) {
	        try {
	            reimbursementDetails.setImage(file.getBytes());
	            reimbursementDetails.setImageName(file.getOriginalFilename());
	            reimbursementDetails.setImageType(file.getContentType());
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle file processing exception
	        }
	    }

	    // Fetch user details using the principal
	    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
	    System.out.println("User Details: " + userDetails);
	    model.addAttribute("user", userDetails);

	    reimbursementService.createFile(reimbursementDetails, userId);
	    return "redirect:/createForm";
	}
	
	@GetMapping("/image")
    public ResponseEntity<ByteArrayResource> getImage(@RequestParam Long id) {
        ReimbursementDetails details = reimbursementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reimbursement not found"));
        System.out.println(">>>>"+details.getImage());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + details.getImageName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, details.getImageType())
                .body(new ByteArrayResource(details.getImage()));
    }
	
	@GetMapping("/download-image/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
		System.out.println("download image filesssss...."+id);
	  Optional<ReimbursementDetails> reimbursement = reimbursementRepository.findById(id);
	  byte[] imageData = reimbursement.get().getImage();
	  String filename = "reimbursement_image_" + id + ".jpg";
	  System.out.println(">>>>>>>print image="+imageData+filename);
	  return ResponseEntity.ok()
	      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	      .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
	      .body(imageData);
	}

}
