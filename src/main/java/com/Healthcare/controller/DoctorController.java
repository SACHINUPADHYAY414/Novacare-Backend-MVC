package com.Healthcare.controller;

import com.Healthcare.model.Doctor;
import com.Healthcare.model.Specialization;
import com.Healthcare.service.DoctorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add-single")
    public ResponseEntity<?> addSingleDoctor(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("stateId") Long stateId,
            @RequestParam("cityId") Long cityId,
            @RequestParam("qualification") String qualification,
            @RequestParam("specializationId") Long specializationId,
            @RequestParam("profileImageUrl") MultipartFile profileImage) {
        try {
            Doctor doctor = new Doctor();
            doctor.setName(name);
            doctor.setGender(gender);
            doctor.setStateId(stateId);
            doctor.setCityId(cityId);
            doctor.setQualification(qualification);
            doctor.setSpecialization(new Specialization(specializationId, null));
            doctor.setStatus(true);

            String imageUrl = doctorService.saveProfileImage(profileImage);
            doctor.setProfileImageUrl(imageUrl);

            Doctor savedDoctor = doctorService.addDoctor(doctor);
            return ResponseEntity.ok(savedDoctor);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid input data",
                    "details", e.getMessage()
            ));
        }
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<?> addDoctorsBulk(@RequestBody String json) {
        try {
            List<Doctor> doctors = objectMapper.readValue(json, new TypeReference<List<Doctor>>() {});
            List<Doctor> savedDoctors = doctorService.addDoctorsBulk(doctors);
            return ResponseEntity.ok(savedDoctors);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid input data",
                    "details", e.getMessage()
            ));
        }
    }

    // ✅ Only active doctors
    @GetMapping("/all")
    public ResponseEntity<?> getAllActiveDoctors() {
        List<Doctor> doctors = doctorService.getAllActiveDoctors();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No active doctors found"));
        }
        return ResponseEntity.ok(doctors);
    }

    // ✅ All doctors (active + inactive)
    @GetMapping("/all-inclusive")
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("stateId") Long stateId,
            @RequestParam("cityId") Long cityId,
            @RequestParam("qualification") String qualification,
            @RequestParam("specializationId") Long specializationId,
            @RequestParam("status") Boolean status,
            @RequestParam(value = "profileImageUrl", required = false) MultipartFile profileImage) {
        try {
            Doctor doctorData = new Doctor();
            doctorData.setName(name);
            doctorData.setGender(gender);
            doctorData.setStateId(stateId);
            doctorData.setCityId(cityId);
            doctorData.setQualification(qualification);
            doctorData.setSpecialization(new Specialization(specializationId, null));
            doctorData.setStatus(status);

            Doctor updatedDoctor = doctorService.updateDoctor(id, doctorData, profileImage);

            if (updatedDoctor == null) {
                return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
            }
            return ResponseEntity.ok(updatedDoctor);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid input data",
                    "details", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        boolean deleted = doctorService.deleteDoctor(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Doctor not found"));
        }
    }
}
