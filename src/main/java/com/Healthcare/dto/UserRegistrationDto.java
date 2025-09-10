package com.healthcare.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String title;
    private String name;
    private String email;
    private String password;
    private String address;
    private Integer city;
    private Integer state;
    private String pinCode;
    private String mobileNumber;
    private String gender;
    private String role;
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getCity() { return city; }
    public void setCity(Integer city) { this.city = city; }

    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}
