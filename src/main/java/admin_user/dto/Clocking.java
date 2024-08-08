package admin_user.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import admin_user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name="clocking")
public class Clocking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
     private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime clockInDate;

    @Column
    private LocalDateTime clockOutDate;
    
    private String status;
    

    public Clocking() {}

    public Clocking(User user, LocalDateTime clockInDate, LocalDateTime clockOutDate) {
        this.user = user;
        this.clockInDate = clockInDate;
        this.clockOutDate = clockOutDate;
    }

    
    
    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getClockInDate() {
        return clockInDate;
    }

    public void setClockInDate(LocalDateTime clockInDate) {
        this.clockInDate = clockInDate;
    }

    public LocalDateTime getClockOutDate() {
        return clockOutDate;
    }

    public void setClockOutDate(LocalDateTime clockOutDate) {
        this.clockOutDate = clockOutDate;
    }

	@Override
	public String toString() {
		return "Clocking [id=" + id + ", user=" + user + ", clockInDate=" + clockInDate + ", clockOutDate="
				+ clockOutDate + "]";
	}
    
    
}