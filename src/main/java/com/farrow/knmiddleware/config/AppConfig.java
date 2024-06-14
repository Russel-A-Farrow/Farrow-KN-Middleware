package com.farrow.knmiddleware.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/* 
 * Use this class to configure Beans that are required by the other config classes 
 */

@Configuration
@PropertySource("classpath:/properties/${deploymentLevel}/application.properties")
public class AppConfig{


}
