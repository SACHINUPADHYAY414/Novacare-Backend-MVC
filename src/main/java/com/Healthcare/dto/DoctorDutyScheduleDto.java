package com.Healthcare.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDutyScheduleDto {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    private String specialization;

    @NotEmpty(message = "Duty duration must not be empty")
    @Valid
    private List<DutySlotDto> duration;

    public DoctorDutyScheduleDto() {
    }

    public DoctorDutyScheduleDto(Long doctorId, String doctorName, String specialization, List<DutySlotDto> duration) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.duration = duration;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<DutySlotDto> getDuration() {
        return duration;
    }

    public void setDuration(List<DutySlotDto> duration) {
        this.duration = duration;
    }

	}