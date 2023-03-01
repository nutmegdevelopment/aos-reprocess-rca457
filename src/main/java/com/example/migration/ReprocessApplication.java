package com.example.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@ConfigurationPropertiesScan(basePackages = "com.example.migration.core")
@SpringBootApplication(scanBasePackages = "com.example.migration.core")
public class ReprocessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReprocessApplication.class, args);
	}

}
