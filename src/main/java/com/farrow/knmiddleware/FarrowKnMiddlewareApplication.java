package com.farrow.knmiddleware;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")},info = @Info(title = "ImagingAPI", version = "1.0.0", description = "Farrow's Imaging API"))
public class FarrowKnMiddlewareApplication {
	
	private static final Logger log = LogManager.getLogger(FarrowKnMiddlewareApplication.class);
	
	public static void main(String[] args) {
		FarrowKnMiddlewareApplication.build(new SpringApplicationBuilder(FarrowKnMiddlewareApplication.class)).run(args);
	}
	


	private static SpringApplicationBuilder build(SpringApplicationBuilder builder) {
		
		if (StringUtils.isBlank(System.getProperty("deploymentLevel"))) { // if deploymentLevel is not provided assume dev
			System.setProperty("deploymentLevel", "development");
		}
		System.setProperty("logging.config", "classpath:properties/"+System.getProperty("deploymentLevel")+"/log4j2.xml");
		
		log.info("deploymentLevel: {}", System.getProperty("deploymentLevel"));
	
		return builder;
	}
}
