-- createShow.sql for ShowRepositoryTest.java
INSERT INTO show
            (id, show_name, no_of_rows, seat_per_rows, ticket_id_allocated, seat_reserved, cancel_in_minutes, start_time, end_time)
VALUES      (123, 'The Tom Hank Show', 26 , 10, 10001, 'A2,A3', 2,  DATE '2022-08-11', DATE '2022-08-11');