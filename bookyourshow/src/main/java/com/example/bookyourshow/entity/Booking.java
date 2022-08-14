package com.example.bookyourshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booking {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", initialValue = 1, allocationSize=1)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    @Column(name = "show_id")
    private String showId;
}