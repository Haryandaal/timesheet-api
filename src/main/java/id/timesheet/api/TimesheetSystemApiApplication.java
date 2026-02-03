package id.timesheet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TimesheetSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetSystemApiApplication.class, args);
	}

}
