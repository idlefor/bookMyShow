package com.example.bookyourshow.repository;

import com.example.bookyourshow.entity.ShowSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    @Query(nativeQuery = true, value = "SELECT ss.* FROM SHOW_SEAT ss WHERE ss.theater_seat_id IN (:seatList)")
    List<ShowSeat> findBySeatListIn(@Param("seatList") List<String> seatList);

    @Query(nativeQuery = true, value = "SELECT ss.theater_seat_id FROM SHOW_SEAT ss WHERE ss.show_id =:showId AND ss.booking_status = 'UNRESERVED'")
    List<String> findUnReservedSeatListByShowId(String showId);

    @Query(nativeQuery = true, value = "SELECT ss.* FROM SHOW_SEAT ss WHERE ss.booking_id =:bookingId")
    List<ShowSeat> findBySeatListByBookingId(String bookingId);
}