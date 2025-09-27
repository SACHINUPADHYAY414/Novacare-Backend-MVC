package com.healthcare.service;

import com.healthcare.dto.AppointmentDetailsDto;
import com.healthcare.dto.BookAppointmentDto;
import com.healthcare.exception.CustomExceptions.ResourceNotFoundException;
import com.healthcare.exception.CustomExceptions.TimeSlotAlreadyBookedException;
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
import java.time.format.DateTimeFormatter;

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
    public BookAppointment bookAppointment(BookAppointmentDto dto, boolean skipEmail) {
        // Check if the time slot is already booked
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

        // Email sending controlled by skipEmail flag
        if (!skipEmail) {
            sendConfirmationEmail(user, doctor, appointment);
        } else {
            System.out.println("⚡ Email skipped due to skipEmail=true");
        }

        return appointment;
    }

    private void sendConfirmationEmail(User user, Doctor doctor, BookAppointment appointment) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return; // ignore if no email
        }

        String subject = "Novacare: Appointment Confirmation";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = appointment.getAppointmentDate().format(dateFormatter);

        String name = (user.getName() != null && !user.getName().trim().isEmpty())
                ? user.getName().trim()
                : "Valued Patient";

        String body = "Dear " + name + ",\n\n"
                + "We're pleased to confirm your appointment at Novacare.\n\n"
                + "Here are the appointment details:\n\n"
                + "👨‍⚕️ Doctor: Dr. " + doctor.getName() + "\n"
                + "📅 Date: " + formattedDate + "\n"
                + "⏰ Time: " + appointment.getAppointmentTime() + "\n"
                + "🔖 Status: " + appointment.getStatus() + "\n\n"
                + "📝 Please arrive 10 minutes early and bring any relevant documents.\n\n"
                + "If you need to modify or cancel your appointment, feel free to contact our support team.\n\n"
                + "Thank you for trusting Novacare with your health.\n\n"
                + "Warm regards,\n"
                + "The Novacare Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public List<AppointmentDetailsDto> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findAppointmentsByUserId(userId);
    }

    public List<AppointmentDetailsDto> getAllAppointments() {
        return appointmentRepository.findAllAppointmentsWithUserAndDoctor();
    }
}
