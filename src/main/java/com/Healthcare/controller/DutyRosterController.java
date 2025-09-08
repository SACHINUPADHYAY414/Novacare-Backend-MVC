package com.Healthcare.controller;

import com.Healthcare.dto.DoctorDutyScheduleDto;
import com.Healthcare.dto.DutyRosterDto;
import com.Healthcare.dto.DutyRosterResponseDto;
import com.Healthcare.model.DutyRoster;
import com.Healthcare.service.DutyRosterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/all")
    public ResponseEntity<List<DutyRosterResponseDto>> getAllDutyRosters() {
        System.out.println("getAllDutyRosters called");

        try {
            List<DutyRoster> dutyRosters = dutyRosterService.getAllRoster();

            List<DutyRosterResponseDto> dtoList = dutyRosters.stream()
                .map(duty -> new DutyRosterResponseDto(
                    duty.getId(),
                    LocalDate.parse(duty.getDutyDate()),
                    duty.getFromTime(),
                    duty.getToTime(),
                    duty.getDuration(),
                    duty.getIsAvailable(),
                    duty.getDoctor() != null ? duty.getDoctor().getId() : null
                ))
                .toList();

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<DutyRoster> updateDutyRoster(
            @PathVariable Long id,
            @Validated @RequestBody DutyRosterDto dto) {
        try {
            DutyRoster updatedRoster = dutyRosterService.updateDutyRoster(id, dto);
            return ResponseEntity.ok(updatedRoster);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
//    @PutMapping("/update/{id}")
//    public ResponseEntity<DutyRoster> updateDutyRoster(
//            @PathVariable Long id,
//            @RequestBody DutyRosterDto dto) {
//        
//        System.out.println("doctorId: " + dto.getDoctorId());
//        System.out.println("duration: " + dto.getDuration());
//        System.out.println("dutyDate: " + dto.getDutyDate());
//        System.out.println("fromTime: " + dto.getFromTime());
//        System.out.println("toTime: " + dto.getToTime());
//        System.out.println("isAvailable: " + dto.getIsAvailable());
//
//        return ResponseEntity.ok().build();
//    }

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
