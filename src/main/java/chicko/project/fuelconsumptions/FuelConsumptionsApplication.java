package chicko.project.fuelconsumptions;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class FuelConsumptionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuelConsumptionsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(IFuelConsumptionDAO fcDAO) {

		return runner -> {
			System.out.println("STARTED");
		};

	}
}
