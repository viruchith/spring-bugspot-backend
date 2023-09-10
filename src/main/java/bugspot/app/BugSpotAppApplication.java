package bugspot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableJpaAuditing
@SpringBootApplication
public class BugSpotAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugSpotAppApplication.class, args);
	}

}
