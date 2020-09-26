package com.dater.model;

import java.time.LocalDate;
import java.util.List;

public class UserBuilder {
	private String username;
	private String email;
	private String password;
	private LocalDate dateOfBirth;
	private String location;
	private String role;
	private Gender gender;
	private Gender preference;
	private String description;
	private List<String> photos;
	
	public UserEntity build() {
		return new UserEntity(this);
	}
	public UserBuilder withUsername(String username) {
		this.username = username;
		return this;
	}
	public UserBuilder withEmail(String email) {
		this.email = email;
		return this;
	}
	public UserBuilder withPassword(String password) {
		this.password = password;
		return this;
	}
	public UserBuilder bornOn(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}
	public UserBuilder withLocation(String location) {
		this.location = location;
		return this;
	}
	public UserBuilder withRole(String role) {
		this.role = role;
		return this;
	}
	public UserBuilder withGender(Gender gender) {
		this.gender = gender;
		return this;
	}
	public UserBuilder withPreference(Gender preference) {
		this.preference = preference;
		return this;
	}
	public UserBuilder withDescription(String description) {
		this.description = description;
		return this;
	}
	public UserBuilder withPhotos(List<String> photos) {
		this.photos = photos;
		return this;
	}
	
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public String getLocation() {
		return location;
	}
	public String getRole() {
		return role;
	}
	public Gender getGender() {
		return gender;
	}
	public Gender getPreference() {
		return preference;
	}
	public String getDescription() {
		return description;
	}
	public List<String> getPhotos() {
		return photos;
	}
}
