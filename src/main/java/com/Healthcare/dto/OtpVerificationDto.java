package com.Healthcare.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationDto {
	  private String email;
	    private String otp;
}
