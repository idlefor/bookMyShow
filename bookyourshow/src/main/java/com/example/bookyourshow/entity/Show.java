package com.example.bookyourshow.entity;

import com.example.bookyourshow.util.ListToStringConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "show")
public class Show {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "show_name")
    private String showName;
    @Column(name = "no_of_rows")
    private Integer noOfRows;
    @Column(name = "seat_per_rows")
    private Integer seatPerRows;

    @Column(name="ticket_id_allocated")
    @Convert(converter = ListToStringConverterUtil.class)
    private List<String> ticketIdAllocated;

    @Column(name="seat_reserved")
    @Convert(converter = ListToStringConverterUtil.class)
    private List<String> seatReserved;

    @Column(name="cancel_in_minutes")
    private Integer cancelInMinutes;

    @JsonIgnore
    private LocalDateTime startTime;
    @JsonIgnore
    private LocalDateTime endTime;
}