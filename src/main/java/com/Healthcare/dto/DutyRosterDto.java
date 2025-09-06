package com.Healthcare.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyRosterDto {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "duration are required")
    @Min(value = 1, message = "duration must be at least 1")
    private Integer duration;

    @NotNull(message = "Duty date is required")
    @FutureOrPresent(message = "Duty date cannot be in the past")
    private LocalDate dutyDate;

    @NotNull(message = "From time is required")
    private LocalTime fromTime;

    @NotNull(message = "To time is required")
    private LocalTime toTime;

    private Boolean isAvailable = true;

    public DutyRosterDto() {
    }

    public DutyRosterDto(Long doctorId, Integer duration, LocalDate dutyDate, LocalTime fromTime, LocalTime toTime, Boolean isAvailable) {
        this.doctorId = doctorId;
        this.duration = duration;
        this.dutyDate = dutyDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.isAvailable = isAvailable;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public boolean isValidTimeRange() {
        return fromTime != null && toTime != null && fromTime.isBefore(toTime);
    }
}