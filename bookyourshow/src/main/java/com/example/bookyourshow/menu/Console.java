package com.example.bookyourshow.menu;

import com.example.bookyourshow.admin.AdminService;
import com.example.bookyourshow.buyer.BuyerService;
import com.example.bookyourshow.exception.NoOfRowExceedLimit;
import com.example.bookyourshow.exception.SeatRowExceedTenException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Console implements CommandLineRunner {

    @Autowired
    private AdminService adminService;
    @Autowired
    private BuyerService buyerService;

    @Override
    public void run(final String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your role [1. ADMIN or 2.BUYER]  key in either number?");
        int roleChoice = scanner.nextInt();
        System.out.println("Your role selected is "+ roleChoice);

        switch (roleChoice) {
            case 1:
                performAdminTask();
                break;
            case 2:
                performBuyerTask();
                break;
            default:
                break;
        }
        scanner.close();
    }

    private void performAdminTask () throws SeatRowExceedTenException, NoOfRowExceedLimit {
        System.out.println("The list of task for admin are : [please select the number according]");
        System.out.println("[OPTION 1. To setup the no of seats and rows per show.]");
        System.out.println("[OPTION 2. To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer.]");
        System.out.println("[OPTION 3. To go back previous menu press this!]");
        final Scanner in = new Scanner(System.in);
        int adminTaskChoice;
        do {
            try {
                adminTaskChoice = in.nextInt(); // Blocks for user input
                if (adminTaskChoice == 1)  {
                    System.out.println("[1. Setup ,<Show Number> <Number of Rows> <Number of Seats per Row> <Cancellation window in minutes>]");
                    System.out.println("[Step 1: Please key in <Show Number> (only number allow)]");
                    Long showNumSetup = Long.parseLong(in.next().trim());
                    System.out.println("[Step 2: Please key in <Number of Rows> (only number allow. max 26)]");
                    Integer noOfRows = Integer.parseInt(in.next().trim());
                    System.out.println("[Step 3: Please key in <Number of Seats per Row> (only number allow max 10)]");
                    Integer noOfSeatsPerRow = Integer.parseInt(in.next().trim());
                    System.out.println("[Step 4: Please key in <Cancellation window in minutes> (only number allow)]");
                    Integer cancellationInMin = Integer.parseInt(in.next().trim());

                    adminService.setupSeatAndRowPerShow(showNumSetup, noOfRows, noOfSeatsPerRow, cancellationInMin);
                    System.out.println("Action completed. press 3 to proceed next");
                } else if (adminTaskChoice == 2)  {
                    System.out.println("[2. View <Show Number>]");
                    System.out.println("[Step 1: Please key in <Show Number> (only number allow)]");
                    long showNum = Long.parseLong(in.next().trim());
                    System.out.println(adminService.viewShowNumber(showNum));;
                    System.out.println("Action completed. press 3 to proceed next");
                } else {
                    run();
                }
            } catch (InputMismatchException e) {
                System.out.println("You have entered an invalid input. Try again.");
                in.next();    // discard non-int input
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void performBuyerTask () throws SeatRowExceedTenException, NoOfRowExceedLimit {
        System.out.println("The list of task for buyer are : [please select the number according]");
        System.out.println("[OPTION 1. To list the available seat numbers for a show. Eg A1, F4 etc]");
        System.out.println("[OPTION 2. To book a ticket. This must generate a unique ticket id and display a message.]");
        System.out.println("[OPTION 3. To cancel a ticket previously booked.]");
        final Scanner in = new Scanner(System.in);
        int buyerTaskChoice;
        do {
            try {
                buyerTaskChoice = in.nextInt(); // Blocks for user input
                if (buyerTaskChoice == 1)  {
                    System.out.println("[1. To list all available seat numbers for a show. Eg. A1 , F4 & etc.]");
                    System.out.println("[Step 1: Please key in the show number of your interest]");
                    String showNum = in.next().trim();
                    System.out.println("Show "+ showNum +" : available seats are "+ buyerService.listAllAvailableSeatForShow(showNum));
                    System.out.println("Action completed. press 4 to proceed next");
                } else if (buyerTaskChoice == 2)  {
                    System.out.println("[2. Book Show <Show Number> <Phone#> <comma separated list of seats>]");
                    System.out.println("[Step 1: Please key in show id (only number allow)]");
                    String showId = in.next().trim();
                    System.out.println("[Step 2: Next please key in phone number (only number allow)]");
                    String phoneNoForBooking = in.next().trim();
                    System.out.println("[Step 3: Lastly please key in seat number follow by comma (no space allow)]");
                    String seatListInCommas = in.next().trim();
                    System.out.println(buyerService.bookSeats(showId, phoneNoForBooking, Arrays.asList(seatListInCommas.split("[, ]+"))));
                    System.out.println("Action completed. press 4 to proceed next");
                } else if (buyerTaskChoice == 3) {
                    System.out.println("[3. Cancel <Ticket#> <Phone#>]");
                    System.out.println("[Step 1: Please key in ticket id (only number allow)]");
                    String ticketId = in.next().trim();
                    System.out.println("[Step 2: Next please key in phone number (only number allow)]");
                    String phoneNoForCancel = in.next().trim();
                    System.out.println(buyerService.cancelTicket(ticketId ,phoneNoForCancel));
                    System.out.println("Action completed. press 4 to proceed next");
                } else {
                    run();
                }
            } catch (InputMismatchException e) {
                System.out.println("You have entered an invalid input. Try again.");
                in.next();    // discard non-int input
            } catch (SeatRowExceedTenException e) {
                throw new SeatRowExceedTenException();
            } catch (NoOfRowExceedLimit noOfRowExceedLimit) {
                throw new NoOfRowExceedLimit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }
}