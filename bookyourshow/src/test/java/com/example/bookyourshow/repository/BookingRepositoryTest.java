package com.example.bookyourshow.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookyourshow.entity.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void verify_injected_autowired_components_are_not_null() {
        assertThat(bookingRepository).isNotNull();
    }

    @DisplayName("Test buyerRepository findByPhoneNo work by using sql script to query and fetch record")
    @Test
    @Sql(scripts={"classpath:createBooking.sql"})
    public void expect_return_result_when_invoke_repo_find_by_phone_no_and_ticketId_if_record_exist () {
        assertThat(bookingRepository.findByPhoneNoAndTicketId("90123546", "000002"))
                .isPresent()
                .map(Booking::getShowId)
                .hasValue("234");
    }

    @DisplayName("Test buyerRepository findByPhoneNo return nothing if no buyer exist in sql script")
    @Test
    @Sql(scripts={"classpath:createBooking.sql"})
    public void expect_no_result_fetch_when_invoke_repo_find_by_phone_no_and_showId_if_record_dont_exist () {
        assertThat(bookingRepository.findByPhoneNoAndTicketId("90029937", "123")).isEmpty();
    }
}