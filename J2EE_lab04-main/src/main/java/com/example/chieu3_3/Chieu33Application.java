package com.example.chieu3_3;

import com.example.chieu3_3.model.Category;
import com.example.chieu3_3.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Chieu33Application {

	public static void main(String[] args) {
		SpringApplication.run(Chieu33Application.class, args);
	}

	@Bean
	public CommandLineRunner init(CategoryService categoryService) {
		return args -> {
			// Add default categories
			categoryService.add(new Category(0, "Laptop"));
			categoryService.add(new Category(0, "Điện thoại"));
		};
	}

}
