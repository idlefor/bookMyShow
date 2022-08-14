package com.example.bookyourshow.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@DataJpaTest
public class ShowRepositoryTest {

    @Autowired
    private ShowRepository showRepository;

    @Test
    public void verify_injected_autowired_components_are_not_null() {
        assertThat(showRepository).isNotNull();
    }

    @DisplayName("Test showRepository findById work by using sql script to query and fetch show")
    @Test
    @Sql(scripts={"classpath:createShow.sql"})
    public void expect_return_result_when_invoke_repo_find_by_id_if_show_exist () {
       assertThat(showRepository.findById(123L))
               .isPresent()
               .map(v -> v.getShowName())
               .hasValue("The Tom Hank Show");
    }

    @DisplayName("Test showRepository findById return nothing if no record exist in sql script")
    @Test
    @Sql(scripts={"classpath:createShow.sql"})
    public void expect_no_result_fetch_when_invoke_repo_find_by_id_if_show_dont_exist () {
        assertThat(showRepository.findById(234L)).isEmpty();
    }
}
