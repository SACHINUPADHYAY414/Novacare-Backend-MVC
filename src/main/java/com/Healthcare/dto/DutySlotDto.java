package com.Healthcare.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutySlotDto {
	
	@NotNull(message = "Duty roster id is required")
	private Long dutyRosterId;
    @NotNull(message = "Duty date is required")
    @FutureOrPresent(message = "Duty date cannot be in the past")
    private LocalDate dutyDate;

    @NotNull(message = "From time is required")
    private LocalTime fromTime;
    private String status; 
    @NotNull(message = "To time is required")
    private LocalTime toTime;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer duration;
    private List<String> bookedAppointmentTimes;

    public DutySlotDto() {
    }

    public DutySlotDto(LocalDate dutyDate, LocalTime fromTime, LocalTime toTime, Integer duration, String status) {
        this.dutyDate = dutyDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.duration = duration;
        this.status = status;
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

    public boolean isValidTimeRange() {
        return fromTime != null && toTime != null && fromTime.isBefore(toTime);
    }

    public Long getDutyRosterId() {
        return dutyRosterId;
    }

    public void setDutyRosterId(Long dutyRosterId) {
        this.dutyRosterId = dutyRosterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getBookedAppointmentTimes() {
        return bookedAppointmentTimes;
    }

    public void setBookedAppointmentTimes(List<String> bookedAppointmentTimes) {
        this.bookedAppointmentTimes = bookedAppointmentTimes;
    }
}
