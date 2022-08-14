package com.example.bookyourshow.buyer;

import com.example.bookyourshow.entity.Booking;
import com.example.bookyourshow.entity.BookingStatus;
import com.example.bookyourshow.entity.Show;
import com.example.bookyourshow.entity.ShowSeat;
import com.example.bookyourshow.exception.CancelTicketNotAllowedException;
import com.example.bookyourshow.exception.ExistingBookingFoundException;
import com.example.bookyourshow.exception.SeatUnavailableException;
import com.example.bookyourshow.repository.BookingRepository;
import com.example.bookyourshow.repository.BuyerRepository;
import com.example.bookyourshow.repository.ShowRepository;
import com.example.bookyourshow.repository.ShowSeatRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service
public class BuyerServiceImpl implements BuyerService {
    private static final String BOOKING_RESERVED = "Booking reserved! Your Ticket ID is : ";
    private static final String SEATS_UNAVAILABLE = "Seats Unavailable";
    private static final String CANCEL_TICKET_NOT_ALLOWED = "Cancel ticket not allow due to technical fault";
    private static final String EXISTING_BOOKING_FOUND_WITH_SAME_PHONE_NO = "Seats book with existing phone no";
    private static final String SEAT_CANCEL_SUCCESSFULLY = " for Seat cancel successfully are : ";
    private static final String SEAT_CANCEL_FAIL = " and for Seat cancel fail due to expiry are : ";

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Value("${cancellation.window.in.minute}")
    private Long configurableCancellationWindowInMinute;

    @Override
    public List<String> listAllAvailableSeatForShow(String showId) {
        List<String> result = showSeatRepository.findUnReservedSeatListByShowId(showId);
        return result.size() > 0 ? result : new ArrayList<>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String bookSeats(String showId, String phoneNo, List<String> seatList) {
        final Integer generatedTicketId = new Random().nextInt(2000000);
        List<ShowSeat> showSeatsList = showSeatRepository.findBySeatListIn(seatList);

        try {
            // Validation I: only 1 booking per phone is allow
            validateIfPhoneNoInUseForSameShow(phoneNo, showId);
            // Validation II: seat status must be unreserved so booking can commence
            validateSeatExistAndAvailable(showSeatsList);

            // Save the generated ticketId to the Database
            saveTicketIdAndSeatReservedToShow(showId, generatedTicketId, showSeatsList);
            // Save the booking to the Database
            Booking booking = saveBooking(showId, phoneNo, generatedTicketId);
            // Seat booking is reserved until it expire after time pass in cancellationWindowInMinute
            List<ShowSeat> selectedSeatsList = changeSeatBookingStatusToReserved(showId, showSeatsList, booking.getId());

            //Save all selected show seat by the buyer to database
            showSeatRepository.saveAllAndFlush(selectedSeatsList);
        } catch (final SeatUnavailableException ex) {
            return SEATS_UNAVAILABLE;
        } catch (final ExistingBookingFoundException ex) {
            return EXISTING_BOOKING_FOUND_WITH_SAME_PHONE_NO;
        }
        return BOOKING_RESERVED + generatedTicketId
                + ". Your selected seats are : " + showSeatsList.stream().map(ShowSeat::getTheaterSeat).collect(Collectors.toList())
                + ". each of them will expire at " + showSeatsList.stream().map(ShowSeat::getExpireTime).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String cancelTicket(String ticketId, String phoneNo) {
        List<String> seatCancelSuccessfully = new ArrayList<>();
        List<String> seatUnableCancelAsExpiryReached = new ArrayList<>();
        try {
            // Find correct booking first using ticket id and phoneNo input
            Optional<Booking> booking = bookingRepository.findByPhoneNoAndTicketId(phoneNo, ticketId);

            if (booking.isPresent()) { // Pick the booking reserved seats related to the booking id
                List<ShowSeat> seatToBeCancelList = showSeatRepository.findBySeatListByBookingId(booking.get().getId().toString());

                if (seatToBeCancelList.size() < 1) {
                    throw new CancelTicketNotAllowedException();
                }
                // If seat is within ticket cancellation timing, we modify the seat's booking status from BOOKING_RESERVED to UNRESERVED
                seatToBeCancelList.forEach(s -> {
                    if (BookingStatus.BOOKING_RESERVED.equals(s.getStatus()) && s.getExpireTime().isAfter(LocalDateTime.now())) {
                        s.setStatus(BookingStatus.UNRESERVED);
                        seatCancelSuccessfully.add(s.getTheaterSeat());
                    } else {
                        seatUnableCancelAsExpiryReached.add(s.getTheaterSeat());
                    }
                });

                // Save this modified show seat list back to database again to make the seat available for booking
                showSeatRepository.saveAllAndFlush(seatToBeCancelList);
                // Remove the ticketId and seat served to the show table as booking is already cancel
                removeTicketIdAndReservedSeatFromShow(ticketId, seatCancelSuccessfully, booking.get());

                // Delete the record in booking table to complete the cancellation process only if all seat can be cancel
                if(seatUnableCancelAsExpiryReached.size() == 0) {
                    bookingRepository.delete(booking.get());
                }
            } else {
                throw new CancelTicketNotAllowedException();
            }
        } catch (CancelTicketNotAllowedException e) {
            return CANCEL_TICKET_NOT_ALLOWED;
        }

        return "Cancellation request for your Ticket ID  : " + ticketId + " under phone no : " + phoneNo
                + SEAT_CANCEL_SUCCESSFULLY + seatCancelSuccessfully
                + SEAT_CANCEL_FAIL + seatUnableCancelAsExpiryReached;
    }

    private void removeTicketIdAndReservedSeatFromShow(String ticketId, List<String> seatCancelSuccessfully, Booking booking) {
        Optional<Show> modifiedShow = showRepository.findById(booking.getId());
        if (modifiedShow.isPresent()) {
            List<String> modifyTicketIdAllocatedList = modifiedShow.get().getTicketIdAllocated()
                    .stream().filter(tix -> !tix.equals(ticketId))
                    .collect(Collectors.toList());

            List<String> modifyAllocatedSeatList = modifiedShow.get().getSeatReserved()
                    .stream().filter(s -> !seatCancelSuccessfully.contains(s))
                    .collect(Collectors.toList());

            // Update the latest value to the database
            modifiedShow.get().setTicketIdAllocated(modifyTicketIdAllocatedList);
            modifiedShow.get().setSeatReserved(modifyAllocatedSeatList);

            showRepository.saveAndFlush(modifiedShow.get());
        }
    }

    private void saveTicketIdAndSeatReservedToShow(String showId, Integer generatedTicketId, List<ShowSeat> seatShowReservedList) {
        Optional<Show> show = showRepository.findById(Long.valueOf(showId));

        // save ticket id and reserved seat to show
        if (show.isPresent()) {
            List<String> modifiedTixList = new ArrayList<>(show.get().getTicketIdAllocated());
            modifiedTixList.add(generatedTicketId.toString());
            List<String> modifiedSeatList = new ArrayList<>(show.get().getSeatReserved());
            modifiedSeatList.addAll(seatShowReservedList.stream().map(ShowSeat::getTheaterSeat).collect(Collectors.toList()));
            Show modifiedShow = Show.builder()
                    .id(show.get().getId())
                    .showName(show.get().getShowName())
                    .seatPerRows(show.get().getSeatPerRows())
                    .noOfRows(show.get().getNoOfRows())
                    .cancelInMinutes(show.get().getCancelInMinutes())
                    .ticketIdAllocated(modifiedTixList)
                    .seatReserved(modifiedSeatList)
                    .startTime(show.get().getStartTime())
                    .endTime(show.get().getEndTime())
                    .build();
            showRepository.saveAndFlush(modifiedShow);
        }
    }

    private List<ShowSeat> changeSeatBookingStatusToReserved(String showId, List<ShowSeat> showSeatsList, Long bookingId) {
        List<ShowSeat> selectedSeatsList = new ArrayList<>();
        for (ShowSeat showSeat: showSeatsList){
            showSeat.setBookingId(bookingId.toString());
            showSeat.setShowId(showId);
            showSeat.setStatus(BookingStatus.BOOKING_RESERVED);
            showSeat.setReservationTime(LocalDateTime.now());
            //default to 2 min but this is configurable in the springboot application properties file to other value if required
            showSeat.setExpireTime(LocalDateTime.now().plusMinutes(
                    configurableCancellationWindowInMinute == null ? 2 : configurableCancellationWindowInMinute));
            selectedSeatsList.add(showSeat);
        }
        return selectedSeatsList;
    }

    private void validateSeatExistAndAvailable(List<ShowSeat> showSeats) throws SeatUnavailableException {
        if (CollectionUtils.isEmpty(showSeats) ||
                showSeats.stream().anyMatch(showSeat -> showSeat.getStatus() != BookingStatus.UNRESERVED)){
            throw new SeatUnavailableException();
        }
    }

    private void validateIfPhoneNoInUseForSameShow(String phoneNo, String showId) throws ExistingBookingFoundException {
        if (bookingRepository.findByPhoneNoAndTicketId(phoneNo, showId).isPresent()){
            throw new ExistingBookingFoundException();
        }
    }

    private Booking saveBooking(String showId, String phoneNo, Integer generatedTicketId) {
        Booking booking = Booking.builder()
                .showId(showId)
                .ticketId(generatedTicketId.toString())
                .phoneNo(phoneNo)
                .build();
        return bookingRepository.saveAndFlush(booking);
    }
}