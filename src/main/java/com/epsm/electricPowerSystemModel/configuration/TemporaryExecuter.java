package com.epsm.electricPowerSystemModel.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.epsm.electricPowerSystemModel.model.generalModel.DispatchingObjectsSource;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class TemporaryExecuter {
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

	private void go(){
		//context.register(TimeService.class);
	
		context.register(ApplicationConfig.class, WebConfig.class);
		context.refresh();
		//DispatchingObjectsSource source = context.getBean(DispatchingObjectsSource.class);
		//System.out.println(source);
		System.out.println("qty= " + context.getBeanDefinitionCount());
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
		
	}
	
	
	
	
	public static void main(String[] args) {
		new TemporaryExecuter().go();
	}
}
