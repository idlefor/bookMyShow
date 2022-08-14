package com.example.bookyourshow.admin;


import com.example.bookyourshow.entity.BookingStatus;
import com.example.bookyourshow.entity.Show;
import com.example.bookyourshow.entity.ShowSeat;
import com.example.bookyourshow.exception.NoOfRowExceedLimit;
import com.example.bookyourshow.exception.SeatRowExceedTenException;
import com.example.bookyourshow.repository.BookingRepository;
import com.example.bookyourshow.repository.ShowRepository;
import com.example.bookyourshow.repository.ShowSeatRepository;
import com.example.bookyourshow.util.DisplayTheaterSeatUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;


    @Override
    @Transactional
    public void setupSeatAndRowPerShow(Long showNum, Integer noOfRows, Integer seatPerRow, Integer cancelInMinutes)
            throws SeatRowExceedTenException, NoOfRowExceedLimit {
        // constraints max seat per row is 10
        if (seatPerRow > 10) {
            throw new SeatRowExceedTenException();
        }
        // max no of row is 26 no more
        if (noOfRows > 26) {
            throw new NoOfRowExceedLimit();
        }

        // Need to store show into into H2 in-memory database
        Show show = Show.builder()
                .id(showNum)
                .cancelInMinutes(cancelInMinutes)
                .noOfRows(noOfRows)
                .seatPerRows(seatPerRow)
                .showName("CastAway")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .cancelInMinutes(cancelInMinutes)
                .build();
        showRepository.saveAndFlush(show);

        // display the plan view of the cinema seat output
        DisplayTheaterSeatUtil displayTheaterSeatUtil = new DisplayTheaterSeatUtil();
        displayTheaterSeatUtil.display(noOfRows, seatPerRow);

        // save all the seat to show_seat table in database
        List<ShowSeat> showSeatList = new ArrayList<>();
        for(int i=1; i <= noOfRows; i++) {
            for(int j=1; j <=seatPerRow; j++) {
                showSeatList.add(ShowSeat.builder()
                        .showId(showNum.toString())
                        .status(BookingStatus.UNRESERVED)
                        .theaterSeat(numericToLetter.get(i)+j)
                        .build());
            }
        }
        showSeatRepository.saveAllAndFlush(showSeatList);
    }

    @Override
    public String viewShowNumber(Long showNum) {
        List<String> phoneNoList = new ArrayList<>();
        Optional<Show> show = showRepository.findById(showNum);
        // only find booking if the show have ticket allocated to it if not skip
        boolean shdDisplayShowDetail = show.isPresent() && show.get().getTicketIdAllocated().size() > 0;
        if (shdDisplayShowDetail) {
            phoneNoList = bookingRepository.findPhoneNoByTicketIdIn(show.get().getTicketIdAllocated());
        }
        return shdDisplayShowDetail ?  formulateMsg(show.get(), showNum, phoneNoList) : "No available Booking found for the show!";
    }

    private String formulateMsg(Show show, Long showNum, List<String> phoneNoList) {
        return "For Show Number [" + showNum + "] Ticket : " + show.getTicketIdAllocated() + " Buyer Phone : " + phoneNoList +
                " Seat Number Allocated to Buyer : " + show.getSeatReserved();
    }

    static Map<Integer, String> numericToLetter = new HashMap<>();
    static {
        numericToLetter.put(1, "A");
        numericToLetter.put(2, "B");
        numericToLetter.put(3, "C");
        numericToLetter.put(4, "D");
        numericToLetter.put(5, "E");
        numericToLetter.put(6, "F");
        numericToLetter.put(7, "G");
        numericToLetter.put(8, "H");
        numericToLetter.put(9, "I");
        numericToLetter.put(10, "J");
        numericToLetter.put(11, "K");
        numericToLetter.put(12, "L");
        numericToLetter.put(13, "M");
        numericToLetter.put(14, "N");
        numericToLetter.put(15, "O");
        numericToLetter.put(16, "P");
        numericToLetter.put(17, "Q");
        numericToLetter.put(18, "R");
        numericToLetter.put(19, "S");
        numericToLetter.put(20, "T");
        numericToLetter.put(21, "U");
        numericToLetter.put(22, "V");
        numericToLetter.put(23, "W");
        numericToLetter.put(24, "X");
        numericToLetter.put(25, "Y");
        numericToLetter.put(26, "Z");
    }
}
