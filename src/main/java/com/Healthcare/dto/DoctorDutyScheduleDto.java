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

    // Remove manual constructors and getters/setters here
}
