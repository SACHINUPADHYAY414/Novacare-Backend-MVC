package com.Healthcare.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Healthcare.model.State;
import com.Healthcare.service.StateService;

@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    private StateService stateService;

    // ✅ Get all states
    @GetMapping
    public List<State> getAllStates() {
        return stateService.getAllStates();
    }

    // ✅ Get state by ID
    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable int id) {
        State state = stateService.getStateById(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Create a single state
    @PostMapping
    public ResponseEntity<State> createState(@RequestBody State state) {
        return ResponseEntity.ok(stateService.createState(state));
    }

    // ✅ Create multiple states (Bulk insert)
    @PostMapping("/bulk")
    public ResponseEntity<List<State>> createStates(@RequestBody List<State> states) {
        return ResponseEntity.ok(stateService.createStates(states));
    }

    // ✅ Delete a state
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteState(@PathVariable int id) {
        stateService.deleteState(id);
        return ResponseEntity.ok("State with ID " + id + " deleted successfully!");
    }
}
