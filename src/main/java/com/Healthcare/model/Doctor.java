package com.Healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    private String name;

    @Column(name = "city_id")
    @JsonProperty("cityId")
    private Long cityId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
    @Column(name = "state_id")
    @JsonProperty("stateId")
    private Long stateId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @JsonProperty("qualification")
    private String qualification;

    @Column(name = "gender")
    @JsonProperty("gender")
    private String gender;

    @Column(name = "profile_image_url")
    @JsonProperty("profileImageUrl")
    private String profileImageUrl;

    // Getter methods (Lombok will generate them, but you can add explicitly if needed)

    public String getSpecializationName() {
        return specialization != null ? specialization.getName() : null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
