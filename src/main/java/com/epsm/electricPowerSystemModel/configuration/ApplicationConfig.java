package com.epsm.electricPowerSystemModel.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ApplicationConfig {
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer 
				= new PropertySourcesPlaceholderConfigurer();
		configurer.setLocation(new ClassPathResource("application.properties"));
        
        return configurer;
    }
}
