package com.healthcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.model.Gender;


public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByName(String name);
}
