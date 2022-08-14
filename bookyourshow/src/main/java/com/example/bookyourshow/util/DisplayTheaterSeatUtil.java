package com.example.bookyourshow.util;

public class DisplayTheaterSeatUtil {

    public DisplayTheaterSeatUtil() {}

    public void display(Integer noOfRows, Integer seatPerRow) {
        System.out.println("       1  2  3  4  5  6  7  8  9  10        ");
        System.out.println("============================================");

        for(int row = 0; row < noOfRows; ++row) {
            System.out.printf("%4d |", row + 1);
            for(int seat = 0; seat < seatPerRow; ++seat) {
                System.out.printf("%3s", "o");
            }
            System.out.println();
        }
        System.out.println("============================================");
    }
}
