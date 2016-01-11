package com.epsm.electricPowerSystemModel.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@ComponentScan("com.epsm.electricPowerSystemModel")
public class TemporaryExecuter {
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	private void go(){
		//context.register(ApplicationConfig.class, WebConfig.class);
		context.refresh();

		System.out.println("qty= " + context.getBeanDefinitionCount());
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
	}
	
	
	
	
	public static void main(String[] args) {
		new TemporaryExecuter().go();
	}
}
