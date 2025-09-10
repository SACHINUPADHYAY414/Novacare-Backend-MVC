package com.healthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthcare.model.Specialization;
import com.healthcare.service.SpecializationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/specialization")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    // Add new specialization
    @PostMapping("/add")
    public ResponseEntity<String> addSpecialization(@RequestBody Specialization specialization) {
        specializationService.save(specialization);
        return ResponseEntity.ok("Specialization added successfully");
    }

    @PostMapping("/addAll")
    public ResponseEntity<String> addSpecializations(@RequestBody List<Specialization> specializations) {
        specializationService.saveAll(specializations);
        return ResponseEntity.ok("Specializations added successfully");
    }

    // Get all specializations
    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecializations() {
        List<Specialization> list = specializationService.getAllSpecializations();
        System.out.println("Specializations fetched: " + list);
        if (list.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "No specializations found"));
        }
        return ResponseEntity.ok(list);
    }

    // Get specialization by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable Long id) {
        Specialization specialization = specializationService.getSpecializationById(id);
        if (specialization == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Specialization not found"));
        }
        return ResponseEntity.ok(specialization);
    }
    
    // Update specialization by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSpecialization(@PathVariable Long id, @RequestBody Specialization updatedSpecialization) {
        Specialization specialization = specializationService.updateSpecialization(id, updatedSpecialization);
        if (specialization == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Specialization not found"));
        }
        return ResponseEntity.ok(Map.of("message", "Specialization updated successfully", "data", specialization));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialization(@PathVariable Long id) {
        boolean deleted = specializationService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Specialization deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Specialization not found");
        }
    }
}
