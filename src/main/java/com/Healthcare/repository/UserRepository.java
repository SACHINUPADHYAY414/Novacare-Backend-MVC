package com.healthcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndOtp(String email, String otp);

    List<User> findAllByOrderByNameAsc(); 
    
    Optional<User> findTopByOrderByIdDesc();
    @Query("SELECT MAX(CAST(SUBSTRING(u.uhid, 5) AS long)) FROM User u WHERE u.uhid LIKE 'UHID%' AND FUNCTION('regexp_match', u.uhid, '^UHID[0-9]+$') IS NOT NULL")
    Long findMaxUhidNumber();

}
