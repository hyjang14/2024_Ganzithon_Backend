package com.ganzithon.Hexfarming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HexfarmingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HexfarmingApplication.class, args);
	}

}
