package com.tishinserg.spreadbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpreadBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadBotApplication.class, args);
	}

}
