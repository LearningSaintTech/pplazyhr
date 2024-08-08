package admin_user.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.dto.TicketDto;
import admin_user.model.User;

public interface TicketRepository extends JpaRepository<TicketDto, Long>{

	List<TicketDto> findByUser(User user);
	List<TicketDto> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

	
}
