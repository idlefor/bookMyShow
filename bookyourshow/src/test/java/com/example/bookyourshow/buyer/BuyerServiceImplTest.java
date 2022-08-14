package com.example.bookyourshow.buyer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.bookyourshow.entity.Booking;
import com.example.bookyourshow.entity.BookingStatus;
import com.example.bookyourshow.entity.Buyer;
import com.example.bookyourshow.entity.Show;
import com.example.bookyourshow.entity.ShowSeat;
import com.example.bookyourshow.menu.Console;
import com.example.bookyourshow.repository.BookingRepository;
import com.example.bookyourshow.repository.BuyerRepository;
import com.example.bookyourshow.repository.ShowRepository;
import com.example.bookyourshow.repository.ShowSeatRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BuyerServiceImplTest {

    @MockBean
    private Console console;

    @InjectMocks
    private BuyerServiceImpl buyerServiceImpl;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private ShowSeatRepository showSeatRepository;


    @DisplayName("validate BuyerServiceImpl.listAllAvailableSeatForShow method able to retrieve list of seats")
    @Test
    public void should_expect_list_all_available_seats_for_show () {
        when(showSeatRepository.findUnReservedSeatListByShowId(any(String.class))).thenReturn(Arrays.asList("A1", "B3", "C9", "D9", "J10"));

        assertEquals(Arrays.asList("A1", "B3", "C9", "D9", "J10"), buyerServiceImpl.listAllAvailableSeatForShow("234"));
    }

    @DisplayName("Testing BuyerServiceImpl.booking method to ensure book successful")
    @Test
    public void should_expect_ticket_booking_succeed_with_message_return_booking_reserved () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(Optional.empty());
        when(showRepository.findById(any(Long.class))).thenReturn(Optional.of(
                Show.builder()
                        .id(1001L)
                        .ticketIdAllocated(Arrays.asList("10001", "10002", "10003", "10004"))
                        .noOfRows(26)
                        .seatPerRows(10)
                        .cancelInMinutes(2)
                        .showName("CastAway")
                        .ticketIdAllocated(Arrays.asList("1092931", "1294982"))
                        .seatReserved(Arrays.asList("A9","C7","I9"))
                        .showName("CastAway")
                        .build()
        ));

        when(buyerRepository.findByPhoneNo(anyString())).thenReturn(Optional.of(
                Buyer.builder()
                        .userName("Tom Hank")
                        .phoneNo("93982771")
                        .build()));
        when(showSeatRepository.findBySeatListIn(any(List.class))).thenReturn(Arrays.asList(
                mockShowSeat(123L, "101", "A9", "10001", BookingStatus.UNRESERVED),
                mockShowSeat(234L, "102", "C7", "10002", BookingStatus.UNRESERVED),
                mockShowSeat(456L, "103","I9", "10003", BookingStatus.UNRESERVED)));
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(Booking.builder()
                .id(10001L)
                .showId("234")
                .phoneNo("93982771")
                .ticketId("10001")
                .build());
        when(showSeatRepository.saveAllAndFlush(any(List.class))).thenReturn(any(List.class));

       assertThat(buyerServiceImpl.bookSeats("234", "93982771", Arrays.asList("A9", "C7", "I9")),
               containsString("Booking reserved! Your Ticket ID is"));
    }

    @DisplayName("Validate BuyerServiceImpl.booking method to throw exception if seat is reserved")
    @Test
    public void should_expect_ticket_booking_failed_with_seat_unavailable_exception_throw () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(Optional.empty());
        when(buyerRepository.findByPhoneNo(anyString())).thenReturn(Optional.of(
                Buyer.builder()
                        .userName("Tom Hank")
                        .phoneNo("93982771")
                        .build()));
        when(showSeatRepository.findBySeatListIn(any(List.class))).thenReturn(Arrays.asList());

        assertEquals("Seats Unavailable",
                buyerServiceImpl.bookSeats("234", "93982771", Arrays.asList("A9", "C7", "I9")));
    }

    @DisplayName("Validate BuyerServiceImpl.booking method to throw exception if booking already made with same phone No")
    @Test
    public void should_expect_ticket_booking_failed_with_existing_booking_found_exception_throw () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(
                Optional.of(Booking.builder().phoneNo("93982771").showId("234").build()));

        assertEquals("Seats book with existing phone no",
                buyerServiceImpl.bookSeats("234", "93982771", Arrays.asList("A9", "C7", "I9")));
    }

    @DisplayName("Validate BuyerServiceImpl.cancelTicket able to cancel a ticket and revert all seat status back to booking unreserved")
    @Test
    public void should_expect_ticket_cancel_success_with_return_message_on_all_seat_status_revert_back_to_booking_unreserved () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(Optional.of(
                Booking.builder()
                        .id(1001L)
                        .phoneNo("90982771")
                        .showId("201")
                        .ticketId("100777")
                        .build()
        ));
        when(showSeatRepository.findBySeatListByBookingId(anyString())).thenReturn(Arrays.asList(
                mockShowSeat(123L, "201","A9", "10004", BookingStatus.BOOKING_RESERVED),
                mockShowSeat(234L, "201","C7", "10004", BookingStatus.BOOKING_RESERVED),
                mockShowSeat(456L, "201","I9", "10004", BookingStatus.BOOKING_RESERVED)));
        when(showSeatRepository.saveAllAndFlush(any(List.class))).thenReturn(Arrays.asList(
                mockShowSeat(123L, "201","A9", "10004", BookingStatus.BOOKING_RESERVED),
                mockShowSeat(234L, "201","C7", "10004", BookingStatus.BOOKING_RESERVED),
                mockShowSeat(456L, "201","I9", "10004", BookingStatus.BOOKING_RESERVED)));

        assertEquals("Cancellation request for your Ticket ID  : 100777 under phone no : 90982771 for Seat cancel successfully are : [A9, C7, I9] " +
                        "and for Seat cancel fail due to expiry are : []",
                buyerServiceImpl.cancelTicket("100777", "90982771"));
    }

    @DisplayName("Validate BuyerServiceImpl.cancelTicket unable to reset seat back to Reserved booking status as they had expire")
    @Test
    public void should_expect_ticket_cancel_fail_with_return_message_on_all_seat_unable_to_cancel () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(Optional.of(
                Booking.builder()
                        .id(1002L)
                        .phoneNo("90982002")
                        .showId("202")
                        .ticketId("100888")
                        .build()
        ));
        when(showSeatRepository.findBySeatListByBookingId(anyString())).thenReturn(Arrays.asList(
                mockShowSeat(123L, "202","G9", "10004", BookingStatus.BOOKING_CONFIRMED),
                mockShowSeat(234L, "202","H7", "10004", BookingStatus.BOOKING_CONFIRMED),
                mockShowSeat(456L, "202","W9", "10004", BookingStatus.BOOKING_CONFIRMED)));
        when(showSeatRepository.saveAllAndFlush(any(List.class))).thenReturn(Arrays.asList(
                mockShowSeat(123L, "202","G9", "10004", BookingStatus.BOOKING_CONFIRMED),
                mockShowSeat(234L, "202","H7", "10004", BookingStatus.BOOKING_CONFIRMED),
                mockShowSeat(456L, "202","W9", "10004", BookingStatus.BOOKING_CONFIRMED)));

        assertEquals("Cancellation request for your Ticket ID  : 100888 under phone no : 90982002 for Seat cancel successfully are : [] " +
                        "and for Seat cancel fail due to expiry are : [G9, H7, W9]",
                buyerServiceImpl.cancelTicket("100888", "90982002"));
    }

    @DisplayName("Validate BuyerServiceImpl.cancelTicket method to throw exception if booking record not exist")
    @Test
    public void should_expect_ticket_cancel_fail_with_exception_throw_due_to_missing_booking_record_not_exist () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(Optional.empty());

        assertEquals("Cancel ticket not allow due to technical fault",
                buyerServiceImpl.cancelTicket("777", "90982771"));
    }

    @DisplayName("Validate BuyerServiceImpl.cancelTicket method to throw exception if cancel seat record not exist")
    @Test
    public void should_expect_ticket_cancel_fail_with_exception_throw_due_to_missing_seat_record_not_exist () {
        when(bookingRepository.findByPhoneNoAndTicketId(anyString(), anyString())).thenReturn(
                Optional.of(Booking.builder().id(9L).build()));
        when(showSeatRepository.findBySeatListByBookingId(anyString())).thenReturn(new ArrayList<>());

        assertEquals("Cancel ticket not allow due to technical fault",
                buyerServiceImpl.cancelTicket("888", "93982213"));
    }

    private ShowSeat mockShowSeat(Long id, String showId, String seat, String bookingId, BookingStatus status) {
        return ShowSeat.builder()
                .id(id)
                .showId(showId)
                .theaterSeat(seat)
                .reservationTime(LocalDateTime.now())
                .expireTime(LocalDateTime.now().plusMinutes(2))
                .bookingId(bookingId)
                .status(status)
                .build();
    }
}