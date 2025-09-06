package com.Healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateDto {
    private int stateId;
    private String name;

    public StateDto() {}

    public StateDto(int stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}