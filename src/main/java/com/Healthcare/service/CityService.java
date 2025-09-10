package com.healthcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.healthcare.model.City;
import com.healthcare.repository.CityRepository;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(int id) {
        return cityRepository.findById(id).orElse(null);
    }

    // Cities by state sorted by name ascending
    public List<City> getCitiesByStateId(int stateId) {
        return cityRepository.findByStateStateId(stateId, Sort.by(Sort.Direction.ASC, "name"));
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public List<City> createCities(List<City> cities) {
        return cityRepository.saveAll(cities);
    }

    public void deleteCity(int id) {
        cityRepository.deleteById(id);
    }
}
