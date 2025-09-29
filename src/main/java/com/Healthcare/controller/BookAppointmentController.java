package com.healthcare.controller;

import com.healthcare.dto.AppointmentDetailsDto;
import com.healthcare.dto.BookAppointmentDto;
import com.healthcare.model.BookAppointment;
import com.healthcare.service.BookAppointmentService;
import com.healthcare.service.RazorpayService;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class BookAppointmentController {

    @Autowired
    private BookAppointmentService appointmentService;

    @Autowired
    private RazorpayService razorpayService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Value("${app.security.skip-otp:false}")
    private boolean skipOtp;

    // DTO class to accept amount in request body
    public static class AmountRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    @PostMapping("/payment/order")
    public ResponseEntity<?> createOrder(@RequestBody AmountRequest request) {
        BigDecimal amount = request.getAmount();
        System.out.println("Received amount: " + request.getAmount());
        if (amount == null) {
            return ResponseEntity.badRequest().body("Required parameter 'amount' is missing");
        }

        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

            int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValueExact();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);  // Razorpay expects amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", UUID.randomUUID().toString());

            Order order = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("/book/after-payment")
    public ResponseEntity<?> verifyAndBookAppointment(@Validated @RequestBody BookAppointmentDto dto) {

        boolean isValid = razorpayService.verifySignature(
                dto.getRazorpayOrderId(),
                dto.getRazorpayPaymentId(),
                dto.getRazorpaySignature()
        );

        if (!isValid) {
            return ResponseEntity.badRequest().body("Payment verification failed");
        }

        BookAppointment appointment = appointmentService.bookAppointment(dto, skipOtp);

        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDetailsDto>> getAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentDetailsDto> appointments = appointmentService.getAppointmentsByUserId(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }
}
