package com.Healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Healthcare.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
