package com.viniciusdev.desafio_itau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesafioItauApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioItauApplication.class, args);
	}

}
