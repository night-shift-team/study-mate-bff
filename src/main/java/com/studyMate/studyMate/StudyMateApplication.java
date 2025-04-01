package com.studyMate.studyMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyMateApplication.class, args);
	}

	@Bean
	public InMemoryHttpExchangeRepository inMemoryHttpExchangeRepository() {
		return new InMemoryHttpExchangeRepository();
	}
}
