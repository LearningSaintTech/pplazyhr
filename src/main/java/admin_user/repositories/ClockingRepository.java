package admin_user.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin_user.dto.Clocking;

@Repository
public interface ClockingRepository extends JpaRepository<Clocking, Long> {
	List<Clocking> findByUserId(Long userId);
	Optional<Clocking> findByUserIdAndClockInDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
