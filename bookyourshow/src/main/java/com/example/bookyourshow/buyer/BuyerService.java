package com.example.bookyourshow.buyer;

import java.util.List;

public interface BuyerService {

    List<String> listAllAvailableSeatForShow(String showId);

    String bookSeats(String showId, String phoneNo, List<String> seatList);

    String cancelTicket(String ticketId, String phoneNo);
}
