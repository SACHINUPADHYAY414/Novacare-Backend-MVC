package com.Healthcare.controller;

import com.Healthcare.model.Doctor;
import com.Healthcare.service.DoctorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    // Add single or multiple doctors dynamically
    @PostMapping("/add")
    public ResponseEntity<?> addDoctor(@RequestBody String json) {
        try {
            if (json.trim().startsWith("[")) {
                // It's a JSON array - bulk insert
                List<Doctor> doctors = objectMapper.readValue(json, new TypeReference<List<Doctor>>() {});
                List<Doctor> savedDoctors = doctorService.addDoctorsBulk(doctors);
                return ResponseEntity.ok(savedDoctors);
            } else {
                // Single doctor object
                Doctor doctor = objectMapper.readValue(json, Doctor.class);
                Doctor savedDoctor = doctorService.addDoctor(doctor);
                return ResponseEntity.ok(savedDoctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid input data", "details", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();

        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No doctors found"));
        }

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
        }

        return ResponseEntity.ok(doctor);
    }
}
