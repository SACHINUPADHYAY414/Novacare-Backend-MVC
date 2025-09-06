package com.Healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Healthcare.model.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findByStateStateId(int stateId);
}
