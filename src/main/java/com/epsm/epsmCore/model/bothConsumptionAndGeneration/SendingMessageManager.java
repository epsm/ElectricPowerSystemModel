package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Dispatcher;

public class SendingMessageManager<T extends Message> {
	private Dispatcher dispatcher;
	private List<T> statesToSend;
	private ArrayList<T> unnecessaryStates;
	private Logger logger;
	
	public SendingMessageManager(Dispatcher dispatcher) {
		logger = LoggerFactory.getLogger(SendingMessageManager.class);
		
		
		if(dispatcher == null){
			String message = "ObjectConnectionManager constructor: dispatcher must not be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.dispatcher = dispatcher;
		statesToSend = Collections.synchronizedList(new ArrayList<T>());
		unnecessaryStates = new ArrayList<T>();
	}
	
	
}
