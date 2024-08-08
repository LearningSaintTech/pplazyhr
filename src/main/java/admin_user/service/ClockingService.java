package admin_user.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.dto.Clocking;
import admin_user.model.User;
import admin_user.repositories.ClockingRepository;

@Service
public class ClockingService {

    @Autowired
    private ClockingRepository clockingRepository;

    public Clocking clockIn(User user) {
    	LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);
        System.out.println("startOfDay >"+startOfDay);
        System.out.println("endOfDay >>>>>"+endOfDay);
        
        Optional<Clocking> existingClocking = clockingRepository.findByUserIdAndClockInDateBetween(user.getId(), startOfDay, endOfDay);
        
        if (existingClocking.isPresent()) {
            Clocking clocking = existingClocking.get();
            clocking.setClockOutDate(LocalDateTime.now());
            return clockingRepository.save(clocking);
        } else {
            Clocking clocking = new Clocking();
            clocking.setUser(user);
            clocking.setClockInDate(LocalDateTime.now());
            clocking.setStatus("Absent");
            return clockingRepository.save(clocking);
        }
    }
    public Clocking clockOut(Long id) {
        Optional<Clocking> clockingOptional = clockingRepository.findById(id);
        if (clockingOptional.isPresent()) {
            Clocking clocking = clockingOptional.get();
            System.out.println("clocking>>>>>>"+clocking);
            clocking.setClockOutDate(LocalDateTime.now());
            Duration duration = Duration.between(clocking.getClockInDate(), clocking.getClockOutDate());
            if (duration.toHours() >= 9 ) {
                clocking.setStatus("Present");
            } else if(duration.toHours() >= 5 && duration.toHours() <= 9) {
                clocking.setStatus("Half-Day");
            }
            else {
            	clocking.setStatus("Absent");
            }

            return clockingRepository.save(clocking);
        } else {
            throw new RuntimeException("Clocking not found");
        }
    }

//    public Optional<Clocking> getClocking(Long id) {
//        return clockingRepository.findById(id);
//    }
    
//    public ClockingService(Long id) {
//    	System.out.println("========"+clockingRepository.findById(id));
//    	clockingRepository.findById(id);
//    	return;
//    }
	public Clocking clockIn() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Clocking> getAllClockings() {
        return clockingRepository.findAll();
    }
	public List<Clocking> getAttendanceByUserId(Long userId) {
        return clockingRepository.findByUserId(userId);
    }
}