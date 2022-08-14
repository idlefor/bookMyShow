package com.example.bookyourshow.admin;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookyourshow.entity.Show;
import com.example.bookyourshow.exception.NoOfRowExceedLimit;
import com.example.bookyourshow.exception.SeatRowExceedTenException;
import com.example.bookyourshow.repository.BookingRepository;
import com.example.bookyourshow.repository.ShowRepository;
import com.example.bookyourshow.repository.ShowSeatRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private ShowSeatRepository showSeatRepository;
    @Mock
    private BookingRepository bookingRepository;

    @DisplayName("To validate if the setupSeatAndRowPerShow() doesn't throw any exception with valid input")
    @Test
    public void should_not_throw_exception_when_invoke_setup_seat_and_row_per_show_with_valid_input () {
        Assertions.assertDoesNotThrow(() -> adminServiceImpl.setupSeatAndRowPerShow(123L, 20, 10,  1));
        Show mockShow = Show.builder()
                .cancelInMinutes(1)
                .seatPerRows(10)
                .noOfRows(20)
                .showName("CastAway")
                .build();

        verify(showRepository, Mockito.times(1)).saveAndFlush(mockShow);
    }

    @DisplayName("To validate if the setupSeatAndRowPerShow() throw SeatRowExceedTenException if seatPerRow > 10")
    @Test
    public void should_throw_seat_row_exceed_ten_exception_when_invoke_setup_seat_and_row_per_show () {
        Assertions.assertThrows(SeatRowExceedTenException.class,
                () -> adminServiceImpl.setupSeatAndRowPerShow(234L, 5, 11,  1));;
    }

    @DisplayName("To validate if the setupSeatAndRowPerShow() throw NoOfRowExceedLimit Exception if noOfRow > 26")
    @Test
    public void should_throw_no_of_row_exceed_ten_exception_when_invoke_setup_seat_and_row_per_show () {
        Assertions.assertThrows(NoOfRowExceedLimit.class,
                () -> adminServiceImpl.setupSeatAndRowPerShow(345L, 30, 10,  1));;
    }

    @DisplayName("To validate if viewShowNumber() work by return show detail")
    @Test
    public void should_return_show_details_when_invoke_view_show_number_if_record_found () {
        when(showSeatRepository.findUnReservedSeatListByShowId(any(String.class))).thenReturn(Arrays.asList("C10", "D7", "E9"));
        when(bookingRepository.findByTicketIdIn(any(List.class))).thenReturn(Arrays.asList("90091203", "90082713"));
        when(showRepository.findById(any())).thenReturn(Optional.of(Show.builder().ticketIdAllocated(Arrays.asList("001", "002")).build()));

        Assertions.assertEquals("For Show Number [345] Ticket : [001, 002] Buyer Phone : [90091203, 90082713] Seat Number Allocated to Buyer : [C10, D7, E9]",
             adminServiceImpl.viewShowNumber(345L));
    }
}