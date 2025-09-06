package com.Healthcare.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Healthcare.model.City;
import com.Healthcare.repository.CityRepository;

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

    public List<City> getCitiesByStateId(int stateId) {
        return cityRepository.findByStateStateId(stateId);
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
