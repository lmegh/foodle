package com.foodle;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class FoodleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodleApplication.class, args);
	}
	
	 @PostConstruct
	 public void init() {
	   TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	 }
}
