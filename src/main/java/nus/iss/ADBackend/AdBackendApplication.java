package nus.iss.ADBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import nus.iss.ADBackend.DataSeedingService.DataSeedingService;

@SpringBootApplication
public class AdBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdBackendApplication.class, args);
	}

	@Component
	@ComponentScan("nus.iss.ADBackend.DataSeedingService")
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		@Autowired
		private DataSeedingService seedDBService;

		@Override
		public void run(String... args) throws Exception {
			seedDBService.launchSeeding();

		}
	}

}
