package com.greenart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScheduledTstApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduledTstApplication.class, args);
	}

}
