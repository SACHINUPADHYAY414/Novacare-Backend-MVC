package com.healthcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.healthcare.model.State;
import com.healthcare.repository.StateRepository;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    // Get all states sorted by name ascending
    public List<State> getAllStates() {
        return stateRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public State getStateById(int id) {
        return stateRepository.findById(id).orElse(null);
    }

    public State createState(State state) {
        return stateRepository.save(state);
    }

    public List<State> createStates(List<State> states) {
        return stateRepository.saveAll(states);
    }

    public void deleteState(int id) {
        stateRepository.deleteById(id);
    }
}
