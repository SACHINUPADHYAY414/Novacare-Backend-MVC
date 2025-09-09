package com.Healthcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Healthcare.model.Specialization;
import com.Healthcare.repository.SpecializationRepository;

import java.util.List;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    // Add in bulk
    public Specialization addSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    // add one
    public void save(Specialization specialization) {
        specializationRepository.save(specialization);
    }


    public void saveAll(List<Specialization> specializations) {
        specializationRepository.saveAll(specializations);
    }
    
    // Get all specializations
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAllByOrderByNameAsc();
    }

    // Get specialization by ID
    public Specialization getSpecializationById(Long id) {
        return specializationRepository.findById(id).orElse(null);
    }
    
    public Specialization updateSpecialization(Long id, Specialization updatedSpecialization) {
        return specializationRepository.findById(id).map(existing -> {
            existing.setName(updatedSpecialization.getName());
            return specializationRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteById(Long id) {
        if (specializationRepository.existsById(id)) {
            specializationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
