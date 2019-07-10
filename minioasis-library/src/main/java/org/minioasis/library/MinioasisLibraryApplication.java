package org.minioasis.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class MinioasisLibraryApplication {

	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		SpringApplication.run(MinioasisLibraryApplication.class, args);
		
	}

}