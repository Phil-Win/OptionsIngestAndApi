package com.philwin.finance.optionsingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OptionsIngestApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptionsIngestApplication.class, args);
	}

}
