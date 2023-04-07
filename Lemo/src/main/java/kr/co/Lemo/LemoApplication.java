package kr.co.Lemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LemoApplication.class, args);
	}

}
