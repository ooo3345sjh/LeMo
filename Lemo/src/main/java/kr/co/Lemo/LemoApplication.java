package kr.co.Lemo;

import kr.co.Lemo.repository.VisitorslogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(LemoApplication.class, args);
	}

}
