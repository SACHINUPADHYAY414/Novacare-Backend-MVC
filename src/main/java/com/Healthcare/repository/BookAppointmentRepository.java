package com.healthcare.repository;

import com.healthcare.model.BookAppointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BookAppointmentRepository extends JpaRepository<BookAppointment, Long> {

    Optional<BookAppointment> findByDutyRosterIdAndAppointmentDateAndAppointmentTime(
        Long dutyRosterId, LocalDate appointmentDate, LocalTime appointmentTime
    );
    List<BookAppointment> findByDutyRosterIdAndAppointmentDate(Long dutyRosterId, LocalDate appointmentDate);

    // Add this method to fetch all appointments for a user
    List<BookAppointment> findByUserId(Long userId);
    List<BookAppointment> findByDutyRosterIdIn(List<Long> dutyRosterIds);

}
