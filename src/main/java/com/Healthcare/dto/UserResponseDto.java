package com.Healthcare.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	 private Long id;
	    private String title;
	    private String name;
	    private String gender;
	    private String email;
	    private String address;
	    private Long city;
	    private Long state;
	    private String pinCode;
	    private String mobileNumber;
	    private String role;
	    public UserResponseDto(Long id, String title, String name, String gender, String email,
	            String address, Long city, Long state, String pinCode,
	            String mobileNumber, String role) {
	this.id = id;
	this.title = title;
	this.name = name;
	this.gender = gender;
	this.email = email;
	this.address = address;
	this.city = city;
	this.state = state;
	this.pinCode = pinCode;
	this.mobileNumber = mobileNumber;
	this.role = role;
	}


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}