package com.healthcare.service;

import com.healthcare.dto.DoctorDutyScheduleDto;
import com.healthcare.dto.DutyRosterDto;
import com.healthcare.dto.DutyRosterResponseDto;
import com.healthcare.dto.DutySlotDto;
import com.healthcare.model.BookAppointment;
import com.healthcare.model.Doctor;
import com.healthcare.model.DutyRoster;
import com.healthcare.repository.BookAppointmentRepository;
import com.healthcare.repository.DutyRosterRepository;
import com.healthcare.repository.DoctorRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DutyRosterService {

    private final DutyRosterRepository dutyRosterRepository;
    private final DoctorRepository doctorRepository;
    private final BookAppointmentRepository bookAppointmentRepository;
    private final EntityManagerFactory entityManagerFactory;

    public DutyRosterService(DutyRosterRepository dutyRosterRepository,
                             DoctorRepository doctorRepository,
                             BookAppointmentRepository bookAppointmentRepository,
                             EntityManagerFactory entityManagerFactory) {
        this.dutyRosterRepository = dutyRosterRepository;
        this.doctorRepository = doctorRepository;
        this.bookAppointmentRepository = bookAppointmentRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    // Create DutyRoster from DTO
    public DutyRoster createDutyRoster(DutyRosterDto dto) {
        DutyRoster dutyRoster = new DutyRoster();

        dutyRoster.setDutyDate(dto.getDutyDate().toString());
        dutyRoster.setFromTime(dto.getFromTime());
        dutyRoster.setToTime(dto.getToTime());
        dutyRoster.setDuration(dto.getDuration());
        dutyRoster.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        dutyRoster.setStatus(dto.getStatus().name());

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + dto.getDoctorId()));
        dutyRoster.setDoctor(doctor);

        return dutyRosterRepository.save(dutyRoster);
    }

    // Get all DutyRoster entities
    public List<DutyRoster> getAllRoster() {
        return dutyRosterRepository.findAll();
    }

    // Get all DutyRoster records as DTO including status
    public List<DutyRosterResponseDto> getAllDutyRosterDtos() {
        List<DutyRoster> dutyRosters = dutyRosterRepository.findAll();

        return dutyRosters.stream().map(dutyRoster -> new DutyRosterResponseDto(
                dutyRoster.getId(),
                LocalDate.parse(dutyRoster.getDutyDate()),
                dutyRoster.getFromTime(),
                dutyRoster.getToTime(),
                dutyRoster.getDuration(),
                dutyRoster.getIsAvailable(),
                dutyRoster.getDoctor().getId(),
                dutyRoster.getStatus()
        )).collect(Collectors.toList());
    }

    // Update DutyRoster
    @Transactional
    public DutyRoster updateDutyRoster(Long id, DutyRosterDto dto) {
        DutyRoster existingRoster = dutyRosterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DutyRoster not found with id: " + id));

        existingRoster.setDutyDate(dto.getDutyDate().toString());
        existingRoster.setFromTime(dto.getFromTime());
        existingRoster.setToTime(dto.getToTime());
        existingRoster.setDuration(dto.getDuration());

        if (dto.getIsAvailable() != null) {
            existingRoster.setIsAvailable(dto.getIsAvailable());
        } else {
            existingRoster.setIsAvailable(true);
        }

        if (dto.getStatus() != null) {
            existingRoster.setStatus(dto.getStatus().name());
        }


        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + dto.getDoctorId()));
            existingRoster.setDoctor(doctor);
        }

        return dutyRosterRepository.save(existingRoster);
    }

    // For debugging Hibernate queries stats
    private void logQueryStats() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        System.out.println("👀 Total SQL Queries Executed: " + stats.getQueryExecutionCount());
        System.out.println("📦 Entity Fetch Count: " + stats.getEntityFetchCount());
        System.out.println("📑 Collections Fetched: " + stats.getCollectionFetchCount());
    }
    
    public List<DoctorDutyScheduleDto> getDoctorDutySchedules(Long doctorId, Long specializationId, String dutyDate) {
        List<DutyRoster> dutyRosters = dutyRosterRepository.searchDutyRoster(doctorId, specializationId, dutyDate);

        List<Long> dutyRosterIds = dutyRosters.stream()
                .map(DutyRoster::getId)
                .collect(Collectors.toList());

        List<BookAppointment> bookedAppointments = new ArrayList<>();
        int batchSize = 100;
        for (int i = 0; i < dutyRosterIds.size(); i += batchSize) {
            List<Long> batch = dutyRosterIds.subList(i, Math.min(i + batchSize, dutyRosterIds.size()));
            bookedAppointments.addAll(bookAppointmentRepository.findByDutyRosterIdIn(batch));
        }

        Map<Long, List<String>> bookedTimesMap = bookedAppointments.stream()
                .collect(Collectors.groupingBy(
                        ba -> ba.getDutyRoster().getId(),
                        Collectors.mapping(ba -> ba.getAppointmentTime().toString(), Collectors.toList())
                ));

        List<DoctorDutyScheduleDto> result = new ArrayList<>();

        Map<Long, List<DutyRoster>> grouped = dutyRosters.stream()
                .collect(Collectors.groupingBy(dr -> dr.getDoctor().getId()));

        for (Map.Entry<Long, List<DutyRoster>> entry : grouped.entrySet()) {
            Long docId = entry.getKey();
            List<DutyRoster> rosters = entry.getValue();

            Doctor doctor = rosters.get(0).getDoctor();

            List<DutySlotDto> duration = rosters.stream().map(roster -> {
                DutySlotDto slot = new DutySlotDto();
                slot.setDutyRosterId(roster.getId());
                slot.setDutyDate(LocalDate.parse(roster.getDutyDate()));
                slot.setFromTime(roster.getFromTime());
                slot.setToTime(roster.getToTime());
                slot.setDuration(roster.getDuration());

                List<String> bookedTimes = bookedTimesMap.getOrDefault(roster.getId(), List.of());
                slot.setBookedAppointmentTimes(bookedTimes);
                slot.setStatus(bookedTimes.isEmpty() ? "AVAILABLE" : "BOOKED");

                return slot;
            }).collect(Collectors.toList());

            DoctorDutyScheduleDto dto = new DoctorDutyScheduleDto();
            dto.setDoctorId(doctor.getId());
            dto.setDoctorName(doctor.getName());
            dto.setSpecialization(
                    doctor.getSpecialization() != null ? doctor.getSpecialization().getName() : "N/A");
            dto.setDuration(duration);

            result.add(dto);
        }

        logQueryStats();

        return result;
    }

}
