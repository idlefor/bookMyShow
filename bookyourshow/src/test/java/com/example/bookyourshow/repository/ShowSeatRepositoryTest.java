package com.example.bookyourshow.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookyourshow.entity.ShowSeat;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


@ActiveProfiles("test")
@DataJpaTest
public class ShowSeatRepositoryTest {

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Test
    public void verify_injected_autowired_components_are_not_null() {
        assertThat(showSeatRepository).isNotNull();
    }

    @DisplayName("Test showSeatRepository findBySeatListIn work by using sql script to query and fetch record")
    @Test
    @Sql(scripts={"classpath:createShowSeatList.sql"})
    public void expect_seat_list_result_fetch_when_invoke_repo_find_by_seat_list () {
        List<ShowSeat> showSeatList =  showSeatRepository.findBySeatListIn(Arrays.asList("A9", "B10", "J3"));
        Assertions.assertEquals(3, showSeatList.size());

        Assertions.assertEquals("A9", showSeatList.get(0).getTheaterSeat());
        Assertions.assertEquals("B10", showSeatList.get(1).getTheaterSeat());
        Assertions.assertEquals("J3", showSeatList.get(2).getTheaterSeat());
    }

    @DisplayName("Test showSeatRepository findBySeatListIn return nothing if no record exist in sql script")
    @Test
    @Sql(scripts={"classpath:createShowSeatList.sql"})
    public void expect_seat_list_result_fetch_nothing_when_invoke_repo_find_by_seat_list () {
        List<ShowSeat> showSeatList =  showSeatRepository.findBySeatListIn(Arrays.asList("C9", "E7", "S8"));
        Assertions.assertEquals(0, showSeatList.size());
    }
}