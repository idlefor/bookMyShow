package com.example.bookyourshow;


import com.example.bookyourshow.menu.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookYourShowApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(BookYourShowApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(BookYourShowApplication.class, args);
		LOG.info("APPLICATION END");
	}

	@Override
	public void run(final String... args) throws Exception {
		LOG.info("Welcome to book your show application");
		Console console = new Console();
		console.handleUserRoleToProcessUserInput();
		LOG.info("Exiting the menu console.");
	}
}