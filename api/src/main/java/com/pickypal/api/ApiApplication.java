package com.pickypal.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

/**
 * @author Queue-ri
 */

@EnableJpaAuditing
@SpringBootApplication

public class ApiApplication {
	
	public static void main(String[] args) throws IOException {
		SpringApplication.run(ApiApplication.class, args);

	}
}
