package com.healthcare.controller;

import com.healthcare.dto.AppointmentDetailsDto;
import com.healthcare.dto.BookAppointmentDto;
import com.healthcare.model.BookAppointment;
import com.healthcare.service.BookAppointmentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class BookAppointmentController {

    @Autowired
    private BookAppointmentService appointmentService;

    // Inject the property from application.properties or config server
    @Value("${app.security.skip-otp:false}")
    private boolean skipOtp;

    @PostMapping("/book")
    public ResponseEntity<BookAppointment> bookAppointment(
            @Validated @RequestBody BookAppointmentDto dto) {

        // Pass skipOtp as skipEmail flag
        BookAppointment appointment = appointmentService.bookAppointment(dto, skipOtp);

        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDetailsDto>> getAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentDetailsDto> appointments = appointmentService.getAppointmentsByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

}
