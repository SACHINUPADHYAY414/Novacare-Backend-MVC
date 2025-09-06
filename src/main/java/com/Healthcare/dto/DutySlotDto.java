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

    // Custom validation logic
    public boolean isValidTimeRange() {
        return fromTime != null && toTime != null && fromTime.isBefore(toTime);
    }
}
