package com.epsm.electricPowerSystemModel.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.generalModel.DispatchingObjectsSource;

@Component
public class IncomingMessageServiceImpl implements IncomingMessageService {
	private Map<Long,DispatchingObject> dispatchingObjects;
	private Logger logger = LoggerFactory.getLogger(IncomingMessageServiceImpl.class);
	
	@Autowired
	private DispatchingObjectsSource source;
	
	@Override
	public void acceptCommand(Command command) {
		if(command != null){
			getDispatchingObjects(command);
			passCommandToObject(command);
		}else{
			logger.warn("Got null command.");
		}
	}
	
	private void getDispatchingObjects(Command command){
		dispatchingObjects = source.getDispatchingObjects();
	}
	
	private void passCommandToObject(Command command){
		Long powerObjectId = command.getPowerObjectId();
		DispatchingObject object = dispatchingObjects.get(powerObjectId);
		
		if(object != null){
			object.executeCommand(command);
			logger.debug("Got {} for power object#{}.",command, powerObjectId);
		}else{
			logger.info("Got command for not existing power object.");
		}
	}
}
