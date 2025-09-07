package com.Healthcare.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.Healthcare.model.City;

public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findByStateStateId(int stateId, Sort sort);
}
