package com.comptel.backend;

import com.comptel.backend.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BackendApplication.class, args);
		ApplicationContext context = SpringApplication.run(BackendApplication.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		System.out.println("UserRepository est prêt : " + userRepository);
	}

}
