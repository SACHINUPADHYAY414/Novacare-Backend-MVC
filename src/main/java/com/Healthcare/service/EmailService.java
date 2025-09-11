package com.healthcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    // Updated method to include userName
    public void sendOtpEmail(String toEmail, String userName, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Novacare: Your Verification OTP");

        // Use userName if provided, else fallback to "Valued User"
        String name = (userName != null && !userName.trim().isEmpty()) ? userName : "Valued User";

        String emailBody = "Dear " + name + ",\n\n"
                + "Thank you for choosing Novacare. To complete your verification process, please use the following One-Time Password (OTP):\n\n"
                + "OTP Code: " + otp + "\n\n"
                + "Please note that this OTP is valid for the next 5 minutes. For your security, do not share this code with anyone.\n\n"
                + "If you did not request this, please ignore this email or contact our support team immediately.\n\n"
                + "Best regards,\n"
                + "The Novacare Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
}
