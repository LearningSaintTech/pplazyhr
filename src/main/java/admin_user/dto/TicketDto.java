package admin_user.dto;

import java.time.LocalDateTime;

import admin_user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name="ticket_dto")
public class TicketDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private String status;
	private LocalDateTime createdAt;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	public User getUser() {
        return user;
    }
	
	

    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public void setUser(User user) {
        this.user = user;
    }
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "TicketDto [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", createdAt=" + createdAt + "]";
	}

	// Getters and setters
	
	
}
