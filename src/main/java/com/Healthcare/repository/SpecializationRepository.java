package com.Healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Healthcare.model.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}
