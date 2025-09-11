package com.healthcare.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_us")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String message;
}
