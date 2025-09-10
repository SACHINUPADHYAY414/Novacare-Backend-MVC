package com.healthcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.healthcare.model.City;
import com.healthcare.service.CityService;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable int id) {
        return cityService.getCityById(id);
    }

    // Cities sorted by name (A to Z) filtered by state
    @GetMapping("/state/{stateId}")
    public List<City> getCitiesByState(@PathVariable int stateId) {
        return cityService.getCitiesByStateId(stateId);
    }

    @PostMapping("/bulk")
    public List<City> createCities(@RequestBody List<City> cities) {
        return cityService.createCities(cities);
    }

    @DeleteMapping("/{id}")
    public String deleteCity(@PathVariable int id) {
        cityService.deleteCity(id);
        return "City with ID " + id + " deleted successfully!";
    }
}
