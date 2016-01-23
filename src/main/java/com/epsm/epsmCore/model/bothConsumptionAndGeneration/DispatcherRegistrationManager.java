package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;

public class DispatcherRegistrationManager {
	private Dispatcher dispatcher;
	private Parameters parameters;
	private Logger logger;
	
	public DispatcherRegistrationManager(Dispatcher dispatcher, Parameters parameters) {
		logger = LoggerFactory.getLogger(DispatcherRegistrationManager.class);
		
		if(dispatcher == null){
			String message = String.format("DispatcherRegistrationManager constructor:"
					+ " dispatcher can't be null.");
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(parameters == null){
			String message = "DispatcherRegistrationManager constructor: parameters"
					+ " can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.dispatcher = dispatcher;
		this.parameters = parameters;
	}

	public boolean registerWithDispatcher(){
		boolean registered = false;
		
		registered =  dispatcher.registerObject(parameters);
		logger.info("{} sent {} to dispatcher and got {}.",
				parameters.getClass().getSimpleName(), registered);
		
		return registered;
	}
}
