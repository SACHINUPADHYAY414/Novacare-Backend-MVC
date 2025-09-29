package com.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAppointmentDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Duty roster ID is required")
    private Long dutyRosterId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    @NotNull(message = "Payment amount is required")
    private BigDecimal amount;

    private String status = "BOOKED";

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
