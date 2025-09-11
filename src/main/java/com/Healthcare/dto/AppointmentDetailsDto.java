package com.healthcare.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import java.time.format.DateTimeFormatter;


 @Data
 public class AppointmentDetailsDto {
		 private Long appointmentId;
		 private Long userId;
		 private String userName;
		 private String userEmail;
		 private Long doctorId;
		 private String doctorName;
		 private Long dutyRosterId;
		 private String status;
		 private String appointmentDate;
		 private String appointmentTime;
		
		 public AppointmentDetailsDto(Long appointmentId, Long userId, String userName, String userEmail,
				    Long doctorId, String doctorName, Long dutyRosterId,
				    String status, LocalDate appointmentDate, LocalTime appointmentTime) {

				    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				    this.appointmentId = appointmentId;
				    this.userId = userId;
				    this.userName = userName;
				    this.userEmail = userEmail;
				    this.doctorId = doctorId;
				    this.doctorName = doctorName;
				    this.dutyRosterId = dutyRosterId;
				    this.status = status;
				    this.appointmentDate = appointmentDate.format(dateFormatter);
				    this.appointmentTime = appointmentTime.toString();
				}


}
