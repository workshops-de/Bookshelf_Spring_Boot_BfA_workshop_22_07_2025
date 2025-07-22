package de.workshops.bookshelf;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookshelfApplication {

	public static void main(String[] args) {
		MDC.put("user", "Birgit");
		SpringApplication.run(BookshelfApplication.class, args);
	}

}
