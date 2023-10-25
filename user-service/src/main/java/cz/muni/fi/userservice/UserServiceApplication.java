package cz.muni.fi.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// For now, I excluded security autoconfiguration, maybe removed after ProtectFilter functionality migration
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
