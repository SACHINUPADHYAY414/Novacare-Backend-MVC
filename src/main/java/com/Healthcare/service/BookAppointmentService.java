package com.healthcare.service;

import com.healthcare.dto.BookAppointmentDto;
import com.healthcare.model.BookAppointment;
import com.healthcare.model.Doctor;
import com.healthcare.model.DutyRoster;
import com.healthcare.model.User;
import com.healthcare.repository.BookAppointmentRepository;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.DutyRosterRepository;
import com.healthcare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.healthcare.exception.CustomExceptions.*;

@Service
public class BookAppointmentService {

    @Autowired
    private BookAppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DutyRosterRepository dutyRosterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public BookAppointment bookAppointment(BookAppointmentDto dto) {
        Optional<BookAppointment> existing = appointmentRepository
                .findByDutyRosterIdAndAppointmentDateAndAppointmentTime(
                        dto.getDutyRosterId(),
                        dto.getAppointmentDate(),
                        dto.getAppointmentTime());

        if (existing.isPresent()) {
            throw new TimeSlotAlreadyBookedException("This time slot is already booked.");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        DutyRoster dutyRoster = dutyRosterRepository.findById(dto.getDutyRosterId())
                .orElseThrow(() -> new ResourceNotFoundException("Duty roster not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BookAppointment appointment = new BookAppointment();
        appointment.setUserId(user.getId());
        appointment.setDoctor(doctor);
        appointment.setDutyRoster(dutyRoster);
        appointment.setStatus(dto.getStatus() != null ? dto.getStatus() : "BOOKED");
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());

        appointmentRepository.save(appointment);

        dutyRoster.setIsAvailable(false);
        dutyRosterRepository.save(dutyRoster);

        sendConfirmationEmail(user, doctor, appointment);

        return appointment;
    }

    private void sendConfirmationEmail(User user, Doctor doctor, BookAppointment appointment) {
        String subject = "Appointment Confirmed";
        String body = "Dear " + user.getName() + ",\n\n" +
                "Your appointment is confirmed with the following details:\n\n" +
                "Doctor: " + doctor.getName() + "\n" +
                "Date: " + appointment.getAppointmentDate() + "\n" +
                "Time: " + appointment.getAppointmentTime() + "\n" +
                "Status: " + appointment.getStatus() + "\n\n" +
                "Thank you for choosing our service!";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public List<BookAppointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }
}
