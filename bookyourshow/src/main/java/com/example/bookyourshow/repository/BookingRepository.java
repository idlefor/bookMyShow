package com.example.bookyourshow.repository;

import com.example.bookyourshow.entity.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(nativeQuery = true, value = "SELECT bb.* FROM BOOKING bb WHERE bb.ticket_id =:ticketId AND bb.phone_no =:phoneNo")
    Optional<Booking> findByPhoneNoAndTicketId(String phoneNo, String ticketId);

    @Query(nativeQuery = true, value = "SELECT bb.phone_no FROM BOOKING bb WHERE bb.ticket_id IN (:ticketId)")
    List<String> findPhoneNoByTicketIdIn(List<String> ticketId);
}
