package com.Healthcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Healthcare.model.Gender;


public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByName(String name);
}
