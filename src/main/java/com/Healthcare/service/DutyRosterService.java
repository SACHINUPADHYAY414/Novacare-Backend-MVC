package com.Healthcare.service;

import com.Healthcare.dto.DoctorDutyScheduleDto;
import com.Healthcare.dto.DutyRosterDto;
import com.Healthcare.dto.DutyRosterResponseDto;
import com.Healthcare.dto.DutySlotDto;
import com.Healthcare.model.BookAppointment;
import com.Healthcare.model.Doctor;
import com.Healthcare.model.DutyRoster;
import com.Healthcare.repository.BookAppointmentRepository;
import com.Healthcare.repository.DutyRosterRepository;
import com.Healthcare.repository.DoctorRepository;
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
        dutyRoster.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ACTIVE");

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

    // ... (rest of your methods remain unchanged)

    // For debugging Hibernate queries stats
    private void logQueryStats() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        System.out.println("👀 Total SQL Queries Executed: " + stats.getQueryExecutionCount());
        System.out.println("📦 Entity Fetch Count: " + stats.getEntityFetchCount());
        System.out.println("📑 Collections Fetched: " + stats.getCollectionFetchCount());
    }
}
