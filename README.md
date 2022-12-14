# Book My Show Application
Booking a show using Java 8 SpringBoot Application

# Tech Stack
1. Java 8 (Pre-Install)
2. Gradle (Pre-Install)
3. H2 (Pre-Install)
4. Spring Boot 2.7.2
5. Lombok

# Class diagram
![image](https://user-images.githubusercontent.com/77923632/184535102-2094f036-ac3c-48e5-9236-49ff1c8e6c74.png)

# How to run
1. Download source code
2. Run BookYourShowApplication.java click on the GREEN PLAY button on the springboot class highlighted in red circle.
![image](https://user-images.githubusercontent.com/77923632/184546262-9fa2bc56-864a-4895-a8f5-d68b683ac855.png)
3. Follow the instruction to select ADMIN or BUYER role and follow the step according.
![image](https://user-images.githubusercontent.com/77923632/184546003-5ddd1d33-6c16-4a10-9bb8-92f835e1cef2.png)


# Use Case

1.  Build a simple Java application for the use case of ‘Booking a Show’. The program must take input from command line.
2.    The program would setup available seats per show, allow buyers to select 1 or more available seats and buy/cancel tickets.
3.   The application shall cater to the below 2 types of users & their requirements - Admin and Buyer
4.    Admin – The users should be able to Setup and view the list of shows and seat allocations.

# Assumptions
For the simplicity of system, the following assumptions was made while implementing the solution -

1. Single User Model - No Concurrency as only One user will enter the command at console issue comman each time. 
2. Single Screen Theaters - No multiple screen handling for a theater has been done. However an option is given for future scope.
3. Admin user can config max up to 10 seats for each show and max row in theater to be 26. Seat numbers are kept fixed in all theaters.
4. No Payment flow used as there is no requirement from the scope
5. Seat book first within the cancellation timing are having booking status as BOOKING_RESERVED
6. No reset of booking seat status for any seat reserved until the springboot is restarted and in memory is wiped. This can be enhanced in future scope.
7. I adopted using spring framework @Transactional to prevent dirty read and obey ACID within any process involving in each buyer booking and cancellation of the ticket.
8. No Add command for ADMIN as the requirement is not clear in the given assignment screenshot
9. We assumed Buyer will input the correct seat number that is within scope of this assignment, if incorrect entry is insert program will ignore them. eg. Z11

# Commands to be implemented for Admin :
1.       Setup  <Show Number> <Number of Rows> <Number of seats per row>  <Cancellation window in minutes>  (To setup the number of seats per show)
2.       View <Show Number>    (To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)
          Buyer – The users should be able retrieve list of available seats for a show     

# Commands to be implemented for Buyer :
1.       Availability  <Show Number>   (To list all available seat numbers for a show. E,g A1, F4 etc)
2.       Book  <Show Number> <Phone#> <Comma separated list of seats> (To book a ticket. This must generate a unique ticket # and display)
3.       Cancel  <Ticket#>  <Phone#> (To cancel a ticket. See contraints in the section below) Select 1 or more seats , buy and cancel tickets.


# Constraints:
1.        Assume max seats per row is 10 and max rows are 26. Example seat number A1,  H5 etc.
2.        The “Add” command for admin must ensure rows cannot be added beyond the upper limit of 26.
3.        After booking, User can cancel the seats within a time window of 2 minutes (configurable).   Cancellation after that is not allowed.
4.        Only one booking per phone# is allowed per show.

# Requirements
1.        Implement the solution as Java standalone application (Java 8+). Can be Springboot as well. The data shall be in-memory.  
2.        Write appropriate Unit Tests.
3.        Implement the above use case considering object oriented principles and development best practices. The implementation should be a tested working executable.  

# Improvements to be made
1.       To add background scheduler job to scan all the records in the database for show_seat to change them from status BOOKING_RESERVED to BOOKING_CONFIRMED after          cancellation time in minute is reach for each booking. 
2.       To add background scheduler job to scan all the record in the database for each end of day at a fixed timing to reset t all booking_status back to                    UNRESERVED so that buyer can book them the next time.
3.       To add more on the fly interractive digraam on the cinema seating plan after any booking or cancelling of the ticket is done at real time.
4.       To utilise of ORM hibernate mapping @JoinColumn to query 2 more more table at the same time instead of one query per table to fetch information from                  different table to reduce number of database call.
          
