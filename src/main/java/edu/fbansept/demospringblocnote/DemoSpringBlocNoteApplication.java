package edu.fbansept.demospringblocnote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoSpringBlocNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBlocNoteApplication.class, args);
	}

}
