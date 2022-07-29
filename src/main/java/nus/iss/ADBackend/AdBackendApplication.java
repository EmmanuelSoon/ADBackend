package nus.iss.ADBackend;

import nus.iss.ADBackend.DataSeedingService.DataSeedingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class AdBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdBackendApplication.class, args);
	}
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		@Autowired
		private DataSeedingService seedDBService;
		@Override
		public void run(String...args) throws Exception{
			seedDBService.launchSeeding();

		}
	}

}
