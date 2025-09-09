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
	    public OtpVerificationDto() {}

	    public OtpVerificationDto(String email, String otp) {
	        this.email = email;
	        this.otp = otp;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getOtp() {
	        return otp;
	    }

	    public void setOtp(String otp) {
	        this.otp = otp;
	    }
}
