package admin_user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import admin_user.dto.Clocking;
import admin_user.model.User;
import admin_user.service.ClockingService;
import admin_user.service.UserService;

@RestController
@RequestMapping("/api/clockings")
public class ClockingController {

    @Autowired
    private ClockingService clockingService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/all")
    public List<Clocking> getAllClockingRecords() {
    	System.out.println("running _------");
        return clockingService.getAllClockings();
    }

//    @GetMapping("/{id}")
//    public Optional<Clocking> getClocking(@PathVariable Long id) {
//    	System.out.println("rrrrrruuuunnnniiiinnngggg"+id);
//        System.out.println("================="+clockingService.getClocking(id));
//    	//return clockingService.getClocking(id).orElseThrow(() -> new RuntimeException("Clocking not found"));
//    	return clockingService.getClocking(id);
//    }
    
    @GetMapping("/{id}")
    public List<Clocking> getAllClockingById(@PathVariable Long id,Model model) {
    	System.out.println("running  33333333333333_------");
        return clockingService.getAttendanceByUserId(id);
    }
    
    @PostMapping("/clock-in/{userId}")
    public Clocking clockIn(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        System.out.println(">>>>>>>>>>>"+userOptional.get().getFullname());
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));
        return clockingService.clockIn(user);
    }

    @PostMapping("/clock-out/{id}")
    public Clocking clockOut(@PathVariable Long id) {
        return clockingService.clockOut(id);
    }

   
    
//    @GetMapping("/allData")
//    public List<Clocking> getAllClockings() {
//    	System.out.println("getting all clocking >>>");
//        return clockingService.getAllClockings();
//    }
}