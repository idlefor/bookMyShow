package com.example.bookyourshow;


import com.example.bookyourshow.repository.BookingRepository;
import com.example.bookyourshow.repository.BuyerRepository;
import com.example.bookyourshow.repository.ShowRepository;
import com.example.bookyourshow.repository.ShowSeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses= {
		BookingRepository.class, ShowRepository.class, ShowSeatRepository.class, BuyerRepository.class})
public class BookYourShowApplication {
	private static Logger LOG = LoggerFactory.getLogger(BookYourShowApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING BOOK YOUR SHOW APPLICATION");
		SpringApplication.run(BookYourShowApplication.class, args);
		LOG.info("APPLICATION END");
	}
}