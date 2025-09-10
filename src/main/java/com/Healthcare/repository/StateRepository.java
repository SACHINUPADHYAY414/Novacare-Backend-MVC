package com.healthcare.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.healthcare.model.State;

public interface StateRepository extends JpaRepository<State, Integer> {

    // Sorted findAll
    List<State> findAll(Sort sort);
}
