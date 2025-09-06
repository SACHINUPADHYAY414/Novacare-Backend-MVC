package com.Healthcare.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Healthcare.model.State;
import com.Healthcare.repository.StateRepository;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    // Get all states
    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    // Get state by ID
    public State getStateById(int id) {
        return stateRepository.findById(id).orElse(null);
    }

    // Create single state
    public State createState(State state) {
        return stateRepository.save(state);
    }

    // ✅ Create multiple states (Bulk insert)
    public List<State> createStates(List<State> states) {
        return stateRepository.saveAll(states);
    }

    // Delete state
    public void deleteState(int id) {
        stateRepository.deleteById(id);
    }
}
