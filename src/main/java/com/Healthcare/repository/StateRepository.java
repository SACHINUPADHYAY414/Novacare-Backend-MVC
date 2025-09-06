package com.Healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Healthcare.model.State;

public interface StateRepository extends JpaRepository<State, Integer> {
}
