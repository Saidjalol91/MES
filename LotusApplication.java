package kr.co.goodstream.lotus.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LotusApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotusApplication.class, args);
	}

}
