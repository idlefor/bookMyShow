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
public class BuyerRepositoryTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Test
    public void verify_injected_autowired_components_are_not_null() {
        assertThat(buyerRepository).isNotNull();
    }

    @DisplayName("Test buyerRepository findByPhoneNo work by using sql script to query and fetch record")
    @Test
    @Sql(scripts={"classpath:createBuyer.sql"})
    public void expect_return_result_when_invoke_repo_find_by_phone_no_if_buyer_exist () {
        assertThat(buyerRepository.findByPhoneNo("90082727"))
                .isPresent()
                .map(v -> v.getUserName())
                .hasValue("Tom Hank");
    }

    @DisplayName("Test buyerRepository findByPhoneNo return nothing if no buyer exist in sql script")
    @Test
    @Sql(scripts={"classpath:createBuyer.sql"})
    public void expect_no_result_fetch_when_invoke_repo_find_by_phone_no_if_buyer_dont_exist () {
        assertThat(buyerRepository.findByPhoneNo("90080007")).isEmpty();
    }
}