package com.dater.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pass_resets")
public class PasswordResetEntity extends BaseEntity implements Persistable<String> {

	@Column(unique = true, nullable = false, updatable = false)
	private String email;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createTime;
	
	@Column(nullable = false)
	private boolean emailSuccessful;

	public PasswordResetEntity(String email, LocalDateTime createTime) {
		this.email = email;
		this.createTime = createTime;
	}

	public PasswordResetEntity() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public boolean isEmailSuccessful() {
		return emailSuccessful;
	}

	public void setEmailSuccessful(boolean successful) {
		this.emailSuccessful = successful;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		//for this entity save is called only for new instances
		return true;
	}
	
}
