package com.healthcare.dto;

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
}
