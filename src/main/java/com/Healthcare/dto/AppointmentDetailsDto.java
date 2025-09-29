package com.healthcare.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import java.time.format.DateTimeFormatter;

@Data
public class AppointmentDetailsDto {

    private Long appointmentId;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long doctorId;
    private String doctorName;
    private Long dutyRosterId;
    private String status;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private BigDecimal amount;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Constructor matching JPQL new expression
    public AppointmentDetailsDto(Long appointmentId,
                                 Long userId, String userName, String userEmail,
                                 Long doctorId, String doctorName,
                                 Long dutyRosterId,
                                 String status,
                                 LocalDate appointmentDate,
                                 LocalTime appointmentTime,
                                 BigDecimal amount) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dutyRosterId = dutyRosterId;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.amount = amount;
    }

    // Getters and setters (if needed)
}
