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
# How to build and run
Steps to build:

1. Download source code
2. Run BookYourShowApplication.java either click on the class to start springBoot button 
![image](https://user-images.githubusercontent.com/77923632/184545965-c5c47fab-1fb0-4ada-804f-d1a282125e79.png)
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
6. No seat status for the seat reserved until the springboot is restarted.
7. I adopted using spring framework @Transactional to prevent dirty read and obey ACID within any process involving in each buyer booking and cancellation of the ticket.
8. No Add command add for ADMIN as the scope is not clear in the given email
9. We assumed Buyer will input the correct seat number that is within scope of this assignment, if incorrect entry is insert program will ignore them

# Commands to be implemented for Admin :

1.       Setup  <Show Number> <Number of Rows> <Number of seats per row>  <Cancellation window in minutes>  (To setup the number of seats per show)
2.       View <Show Number>    (To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)
          Buyer – The users should be able retrieve list of available seats for a show     

# Commands to be implemented for Buyer :
1.       Availability  <Show Number>   (To list all available seat numbers for a show. E,g A1, F4 etc)
2.       Book  <Show Number> <Phone#> <Comma separated list of seats> (To book a ticket. This must generate a unique ticket # and display)
3.       Cancel  <Ticket#>  <Phone#> (To cancel a ticket. See contraints in the section below) Select 1 or more seats , buy and cancel tickets.


# Constraints:
·         Assume max seats per row is 10 and max rows are 26. Example seat number A1,  H5 etc.
The “Add” command for admin must ensure rows cannot be added beyond the upper limit of 26.
·         After booking, User can cancel the seats within a time window of 2 minutes (configurable).   Cancellation after that is not allowed.
·         Only one booking per phone# is allowed per show.

# Requirements
1.        Implement the solution as Java standalone application (Java 8+). Can be Springboot as well. The data shall be in-memory.  
2.        Write appropriate Unit Tests.
3.        Implement the above use case considering object oriented principles and development best practices. The implementation should be a tested working executable.  

# Improvements to be made outside the assignment scope
1.       To add scheduler job to scan all the record in the database for show_seat so that those booking status BOOKING_RESERVED after the cancellation time is up can 
         to become BOOKING_CONFIRMED. 
2.       To add scheduler job to scan all the record in the database for show_seat that have show ended need go to all the seat and rest all booking_status back to            UNRESERVED by the end of a day.
3.       To validate the buyer input when booking seats and reject the incorrect entry eg. Z19 as max no of seat per row is 10 only
4.       Use hibernate mapping @JoinColumn to query 2 more more table at the same time instead of one query per table to fetch information from different table
          
