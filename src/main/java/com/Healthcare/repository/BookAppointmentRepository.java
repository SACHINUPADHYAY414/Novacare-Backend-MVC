package com.healthcare.repository;

import com.healthcare.dto.AppointmentDetailsDto;
import com.healthcare.model.BookAppointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface BookAppointmentRepository extends JpaRepository<BookAppointment, Long> {

    Optional<BookAppointment> findByDutyRosterIdAndAppointmentDateAndAppointmentTime(
        Long dutyRosterId, LocalDate appointmentDate, LocalTime appointmentTime
    );
    List<BookAppointment> findByDutyRosterIdAndAppointmentDate(Long dutyRosterId, LocalDate appointmentDate);

    
    List<BookAppointment> findByUserId(Long userId);
    List<BookAppointment> findByDutyRosterIdIn(List<Long> dutyRosterIds);
    @Query("SELECT new com.healthcare.dto.AppointmentDetailsDto(" +
    	       "b.id, u.id, u.name, u.email, " +
    	       "d.id, d.name, b.dutyRoster.id, " +
    	       "b.status, b.appointmentDate, b.appointmentTime) " +
    	       "FROM BookAppointment b " +
    	       "JOIN b.doctor d " +
    	       "JOIN b.user u")
    	List<AppointmentDetailsDto> findAllAppointmentsWithUserAndDoctor();


}
