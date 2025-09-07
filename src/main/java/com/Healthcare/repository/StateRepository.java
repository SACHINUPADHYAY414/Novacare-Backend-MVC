package com.Healthcare.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.Healthcare.model.State;

public interface StateRepository extends JpaRepository<State, Integer> {

    // Sorted findAll
    List<State> findAll(Sort sort);
}
