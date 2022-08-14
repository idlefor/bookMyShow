-- createShowSeatList.sql for ShowSeatRepositoryTest.java
INSERT INTO show_seat
            (id, show_id, reservation_time, expire_time, theater_seat_id, booking_status)
VALUES      ( 1, '666', DATE '2022-08-11', DATE '2022-08-11', 'A9','BOOKING_CONFIRMED');

INSERT INTO show_seat
            (id, show_id,  reservation_time, expire_time, theater_seat_id, booking_status)
VALUES      ( 2, '666', DATE '2022-08-11', DATE '2022-08-11', 'B10','BOOKING_CONFIRMED');

INSERT INTO show_seat
            (id,show_id, reservation_time, expire_time, theater_seat_id, booking_status)
VALUES      ( 3, '666', DATE '2022-08-11', DATE '2022-08-11', 'J3','BOOKING_CONFIRMED');