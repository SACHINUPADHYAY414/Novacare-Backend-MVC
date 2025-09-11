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
import org.springframework.beans.factory.annotation.Value;

@Service
public class DutyRosterService {
	
    private final DutyRosterRepository dutyRosterRepository;
    private final DoctorRepository doctorRepository;
    private final BookAppointmentRepository bookAppointmentRepository;
    private final EntityManagerFactory entityManagerFactory;
    @Value("${duty.search.default-duration-years:2}")
    private int defaultDurationYears;

    @Value("${duty.search.specialization-duration-months:2}")
    private int specializationDurationMonths;

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
        String currentDate = null, endDate = null;

        if (dutyDate == null) {
            currentDate = LocalDate.now().toString();

            if (specializationId != null) {
                endDate = LocalDate.now().plusMonths(specializationDurationMonths).toString();
            } else {
                endDate = LocalDate.now().plusYears(defaultDurationYears).toString();
            }
        }

        
        List<DutyRoster> dutyRosters = dutyRosterRepository.searchDutyRoster(
            doctorId, specializationId, dutyDate, currentDate, endDate);

        List<Long> ids = dutyRosters.stream()
            .map(DutyRoster::getId)
            .collect(Collectors.toList());

        List<BookAppointment> booked = new ArrayList<>();
        int batch = 100;
        for (int i = 0; i < ids.size(); i += batch) {
            var part = ids.subList(i, Math.min(i + batch, ids.size()));
            booked.addAll(bookAppointmentRepository.findByDutyRosterIdIn(part));
        }

        Map<Long, List<String>> bookedMap = booked.stream()
            .collect(Collectors.groupingBy(
                ba -> ba.getDutyRoster().getId(),
                Collectors.mapping(ba -> ba.getAppointmentTime().toString(), Collectors.toList())
            ));

        List<DoctorDutyScheduleDto> result = new ArrayList<>();
        var grouped = dutyRosters.stream()
            .collect(Collectors.groupingBy(dr -> dr.getDoctor().getId()));

        for (var entry : grouped.entrySet()) {
            var docId = entry.getKey();
            var rosters = entry.getValue();
            var doctor = rosters.get(0).getDoctor();

            List<DutySlotDto> slots = rosters.stream().map(r -> {
                DutySlotDto slot = new DutySlotDto();
                slot.setDutyRosterId(r.getId());
                slot.setDutyDate(LocalDate.parse(r.getDutyDate()));
                slot.setFromTime(r.getFromTime());
                slot.setToTime(r.getToTime());
                slot.setDuration(r.getDuration());

                var times = bookedMap.getOrDefault(r.getId(), List.of());
                slot.setBookedAppointmentTimes(times);
                slot.setStatus(times.isEmpty() ? "AVAILABLE" : "BOOKED");
                return slot;
            }).toList();

            DoctorDutyScheduleDto dto = new DoctorDutyScheduleDto();
            dto.setDoctorId(doctor.getId());
            dto.setDoctorName(doctor.getName());
            dto.setSpecialization(doctor.getSpecialization() != null
                ? doctor.getSpecialization().getName() : "N/A");
            dto.setDuration(slots);
            result.add(dto);
        }

        logQueryStats();
        return result;
    }

}
