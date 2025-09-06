package com.Healthcare.controller;

import com.Healthcare.dto.DoctorDutyScheduleDto;
import com.Healthcare.dto.DutyRosterDto;
import com.Healthcare.model.DutyRoster;
import com.Healthcare.service.DutyRosterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duty-roster")
public class DutyRosterController {

    private final DutyRosterService dutyRosterService;

    public DutyRosterController(DutyRosterService dutyRosterService) {
        this.dutyRosterService = dutyRosterService;
    }

    @PostMapping("/add")
    public ResponseEntity<List<DutyRoster>> createRoster(@Validated @RequestBody List<DutyRosterDto> dtoList) {
        List<DutyRoster> savedRosters = dtoList.stream()
                                               .map(dutyRosterService::createDutyRoster)
                                               .toList();
        return ResponseEntity.ok(savedRosters);
    }

    @GetMapping
    public ResponseEntity<List<DutyRoster>> getAllRoster() {
        return ResponseEntity.ok(dutyRosterService.getAllRoster());
    }

    @GetMapping("/search-doctor")
    public ResponseEntity<List<DoctorDutyScheduleDto>> searchDoctorDutySchedule(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long specializationId,
            @RequestParam(required = false) String dutyDate) {

        System.out.println("Received search params - doctorId: " + doctorId + 
                           ", specializationId: " + specializationId + 
                           ", dutyDate: " + dutyDate);

        List<DoctorDutyScheduleDto> result = dutyRosterService.getDoctorDutySchedules(doctorId, specializationId, dutyDate);

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(List.of());
        }

        return ResponseEntity.ok(result);
    }


}
