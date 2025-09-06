package com.Healthcare.repository;

import com.Healthcare.model.DutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DutyRosterRepository extends JpaRepository<DutyRoster, Long> {

    @Query("SELECT dr FROM DutyRoster dr " +
    	       "JOIN FETCH dr.doctor d " +
    	       "LEFT JOIN FETCH d.specialization s " +
    	       "WHERE (:doctorId IS NULL OR d.id = :doctorId) " +
    	       "AND (:specializationId IS NULL OR s.id = :specializationId) " +
    	       "AND (:dutyDate IS NULL OR dr.dutyDate = :dutyDate)")
    	List<DutyRoster> searchDutyRoster(@Param("doctorId") Long doctorId,
    	                                  @Param("specializationId") Long specializationId,
    	                                  @Param("dutyDate") String dutyDate);
    @Query("SELECT COUNT(ba) > 0 FROM BookAppointment ba WHERE ba.dutyRoster.id = :dutyRosterId")
    boolean isDutyRosterBooked(@Param("dutyRosterId") Long dutyRosterId);

}
