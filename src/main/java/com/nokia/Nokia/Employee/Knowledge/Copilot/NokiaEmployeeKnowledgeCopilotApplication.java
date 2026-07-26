package com.nokia.Nokia.Employee.Knowledge.Copilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NokiaEmployeeKnowledgeCopilotApplication {

	public static void main(String[] args) {
		SpringApplication.run(NokiaEmployeeKnowledgeCopilotApplication.class, args);
	}

}
