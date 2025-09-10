package com.healthcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthcare.model.State;
import com.healthcare.service.StateService;

@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping
    public List<State> getAllStates() {
        return stateService.getAllStates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable int id) {
        State state = stateService.getStateById(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<State> createState(@RequestBody State state) {
        return ResponseEntity.ok(stateService.createState(state));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<State>> createStates(@RequestBody List<State> states) {
        return ResponseEntity.ok(stateService.createStates(states));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteState(@PathVariable int id) {
        stateService.deleteState(id);
        return ResponseEntity.ok("State with ID " + id + " deleted successfully!");
    }
}
