package com.healthcare.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.model.ContactUs;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
}
