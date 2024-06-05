package com.devcv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevcvApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevcvApplication.class, args);
	}

}
