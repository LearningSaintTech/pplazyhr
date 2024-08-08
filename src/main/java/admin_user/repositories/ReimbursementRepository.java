package admin_user.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.dto.ReimbursementDetails;
import admin_user.model.User;

public interface ReimbursementRepository extends JpaRepository<ReimbursementDetails, Long>{
	
	List<ReimbursementDetails> findByUser(User user);
	List<ReimbursementDetails> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

}
