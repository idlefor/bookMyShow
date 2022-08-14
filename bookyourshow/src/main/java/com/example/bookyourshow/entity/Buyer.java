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
@Table(name = "buyer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Buyer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="buyer_seq")
    @SequenceGenerator(name = "buyer_seq", sequenceName = "buyer_seq", initialValue = 1, allocationSize=1)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_no")
    private String phoneNo;
}
