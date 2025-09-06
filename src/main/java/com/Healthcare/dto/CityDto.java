package com.Healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private int cityId;
    private String name;

    public CityDto() {}

    public CityDto(int cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}