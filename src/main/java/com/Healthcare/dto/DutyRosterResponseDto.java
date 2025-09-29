package com.healthcare.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class DutyRosterResponseDto {

    private Long id;
    private LocalDate dutyDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private Integer duration;
    private Boolean isAvailable;
    private Long doctorId;
    private String status;
    private BigDecimal amount;
    
    // Add this field in all constructors, getters and setters
    public DutyRosterResponseDto(Long id, LocalDate dutyDate, LocalTime fromTime, LocalTime toTime,
            Integer duration, Boolean isAvailable, Long doctorId, String status, BigDecimal amount) {
        this.id = id;
        this.dutyDate = dutyDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.duration = duration;
        this.isAvailable = isAvailable;
        this.doctorId = doctorId;
        this.status = status;
        this.amount = amount;
    }

    // Getter and Setter for amount
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
