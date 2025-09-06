package com.Healthcare.service;

import com.Healthcare.dto.DoctorDutyScheduleDto;
import com.Healthcare.dto.DutySlotDto;
import com.Healthcare.model.BookAppointment;
import com.Healthcare.model.Doctor;
import com.Healthcare.model.DutyRoster;
import com.Healthcare.repository.BookAppointmentRepository;
import com.Healthcare.repository.DutyRosterRepository;
import com.Healthcare.repository.DoctorRepository;
import jakarta.persistence.EntityManagerFactory;
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
    private final EntityManagerFactory entityManagerFactory; // For Hibernate stats

    public DutyRosterService(DutyRosterRepository dutyRosterRepository,
                             DoctorRepository doctorRepository,
                             BookAppointmentRepository bookAppointmentRepository,
                             EntityManagerFactory entityManagerFactory) {
        this.dutyRosterRepository = dutyRosterRepository;
        this.doctorRepository = doctorRepository;
        this.bookAppointmentRepository = bookAppointmentRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<DoctorDutyScheduleDto> getDoctorDutySchedules(Long doctorId, Long specializationId, String dutyDate) {
        // Fetch duty rosters matching criteria
        List<DutyRoster> dutyRosters = dutyRosterRepository.searchDutyRoster(doctorId, specializationId, dutyDate);

        // Collect duty roster IDs for batch fetching booked appointments
        List<Long> dutyRosterIds = dutyRosters.stream()
                .map(DutyRoster::getId)
                .collect(Collectors.toList());

        // Batch fetch BookAppointments to avoid huge IN clause
        List<BookAppointment> bookedAppointments = new ArrayList<>();
        int batchSize = 100;
        for (int i = 0; i < dutyRosterIds.size(); i += batchSize) {
            List<Long> batch = dutyRosterIds.subList(i, Math.min(i + batchSize, dutyRosterIds.size()));
            bookedAppointments.addAll(bookAppointmentRepository.findByDutyRosterIdIn(batch));
        }

        // Map dutyRosterId -> list of booked appointment times (as Strings)
        Map<Long, List<String>> bookedTimesMap = bookedAppointments.stream()
                .collect(Collectors.groupingBy(
                        ba -> ba.getDutyRoster().getId(),
                        Collectors.mapping(ba -> ba.getAppointmentTime().toString(), Collectors.toList())
                ));

        List<DoctorDutyScheduleDto> result = new ArrayList<>();

        // Group duty rosters by doctorId to avoid N+1 query problem
        Map<Long, List<DutyRoster>> grouped = dutyRosters.stream()
                .collect(Collectors.groupingBy(dr -> dr.getDoctor().getId()));

        for (Map.Entry<Long, List<DutyRoster>> entry : grouped.entrySet()) {
            Long docId = entry.getKey();
            List<DutyRoster> rosters = entry.getValue();

            // Since all rosters have the same doctor, pick first to get Doctor object
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

        // Log Hibernate query statistics to monitor SQL queries executed
        logQueryStats();

        return result;
    }

    // Utility method to log Hibernate SQL query stats
    private void logQueryStats() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        System.out.println("👀 Total SQL Queries Executed: " + stats.getQueryExecutionCount());
        System.out.println("📦 Entity Fetch Count: " + stats.getEntityFetchCount());
        System.out.println("📑 Collections Fetched: " + stats.getCollectionFetchCount());
    }
}
