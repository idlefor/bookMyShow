package com.example.bookyourshow.admin;

import com.example.bookyourshow.exception.NoOfRowExceedLimit;
import com.example.bookyourshow.exception.SeatRowExceedTenException;

public interface AdminService {

    void setupSeatAndRowPerShow(Long showNum, Integer noOfRows, Integer seatPerRow, Integer cancelInMinutes)
            throws SeatRowExceedTenException, NoOfRowExceedLimit;

    String viewShowNumber(Long showNum);
}
