package com.example.bookyourshow.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "show_seat")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowSeat {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="showseat_seq")
    @SequenceGenerator(name = "showseat_seq", sequenceName = "showseat_seq", initialValue = 1, allocationSize=1)
    private Long id;

    @Column(name = "show_id")
    private String showId;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "theater_seat_id")
    private String theaterSeat;

    @Column (name = "booking_status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "booking_id")
    private String bookingId;
}