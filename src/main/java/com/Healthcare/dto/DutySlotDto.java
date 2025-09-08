package com.Healthcare.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DutySlotDto {

    @NotNull(message = "Duty roster id is required")
    private Long dutyRosterId;

    @NotNull(message = "Duty date is required")
    @FutureOrPresent(message = "Duty date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dutyDate;

    @NotNull(message = "From time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime fromTime;

    @NotNull(message = "To time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime toTime;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer duration;

    private List<String> bookedAppointmentTimes;

    private String status;

    // ======= Getters and Setters =======

    public Long getDutyRosterId() {
        return dutyRosterId;
    }

    public void setDutyRosterId(Long dutyRosterId) {
        this.dutyRosterId = dutyRosterId;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<String> getBookedAppointmentTimes() {
        return bookedAppointmentTimes;
    }

    public void setBookedAppointmentTimes(List<String> bookedAppointmentTimes) {
        this.bookedAppointmentTimes = bookedAppointmentTimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ======= Custom Validation =======

    public boolean isValidTimeRange() {
        return fromTime != null && toTime != null && fromTime.isBefore(toTime);
    }
}
