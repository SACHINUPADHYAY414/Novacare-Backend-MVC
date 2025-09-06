package com.Healthcare.controller;

import com.Healthcare.dto.BookAppointmentDto;
import com.Healthcare.model.BookAppointment;
import com.Healthcare.service.BookAppointmentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class BookAppointmentController {

    @Autowired
    private BookAppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<BookAppointment> bookAppointment(
            @Validated @RequestBody BookAppointmentDto dto) {
        BookAppointment appointment = appointmentService.bookAppointment(dto);
        return ResponseEntity.ok(appointment);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookAppointment>> getAppointmentsByUser(@PathVariable Long userId) {
        List<BookAppointment> appointments = appointmentService.getAppointmentsByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

}
