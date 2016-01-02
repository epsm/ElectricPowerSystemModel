package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ObjectConnectionManager{
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private MessageFilter filter;
	private volatile LocalDateTime timeWhenRecievedLastMessage;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private long objectId;
	private String objectClass;
	private Logger logger;

	public ObjectConnectionManager(TimeService timeService,	Dispatcher dispatcher, PowerObject object) {
		if(timeService == null){
			String message = String.format("ObjectConnectionManager constructor: timeService must "
					+ "not be null.");
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "ObjectConnectionManager constructor: dispatcher must not be null.";
			throw new DispatchingException(message);
		}else if(object == null){
			String message = "ObjectConnectionManager constructor: PowerObject must not be null.";
			throw new DispatchingException(message);
		}
		
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.object = object;
		objectClass = object.getClass().getSimpleName();
		filter = new MessageFilter(object.getClass());
		objectId = object.getId();
		timeWhenRecievedLastMessage = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(ObjectConnectionManager.class);
	}
	
	public void process(Message message) {
		if(message == null){
			logger.warn("ObjectConnectionManager#{} recieved null from dispatcher.", objectId);
		}else if(isCommandMessageTypeEqualsToExpected(message)){
			setTimeWhenReceivedLastMessage();
			object.executeCommand(message);
			
			logger.info("ObjectConnectionManager#{} recieved {} from dispatcher.", 
					objectId, getMessageClassName(message));
		}else{
			logger.warn("ObjectConnectionManager#{} recieved from dispatcher wrong message class: "
					+ "expected {}, but was {}.", objectId, filter.getExpectedCommandMessageClassName(),
					getMessageClassName(message));
		}
	}
	
	private String getMessageClassName(Message message){
		return message.getClass().getSimpleName();
	}
	
	private boolean isCommandMessageTypeEqualsToExpected(Message message){
		return filter.isCommandMessageValid(message);
	}
	
	private void setTimeWhenReceivedLastMessage(){
		timeWhenRecievedLastMessage = timeService.getCurrentTime();
	}
	
	public final void sendMessageIfItNecessary(){
		
		getCurrentTime();
		
		if(isConnectionWithDispatcherActive()){
			if(isItTimeToSentMessage()){
				sendStateToDispatcher();
				setTimeWhenSentLastMessage();
			}
		}else{
			establishConnectionToDispatcher();
		}
	}
	
	private void getCurrentTime(){
		currentTime = timeService.getCurrentTime();
	}
	
	private boolean isConnectionWithDispatcherActive(){
		return timeWhenRecievedLastMessage.plusSeconds(
				GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS)
				.isAfter(currentTime);
	}
	
	private boolean isItTimeToSentMessage(){
		return timeWhenSentLastMessage.plusSeconds(
				GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS)
				.isBefore(currentTime);
	}
	
	private void sendStateToDispatcher(){
		Message state = object.getState();
		
		if(state == null){
			String message =  String.format("%s#%d returned null instead %s.", objectClass,
					objectId, filter.getExpectedStateMessageClassName());
			throw new DispatchingException(message);
		}else if(isStateMessageTypeEqualsToExpected(state)){
			dispatcher.acceptMessage(state);
			
			logger.info("%s#{} sent {} to dispatcher.", objectId, objectClass,
					filter.getExpectedStateMessageClassName());
		}else{
			String message = String.format("%s#%d returned %s instead %s.", objectClass,
					objectId, getMessageClassName(state), filter.getExpectedStateMessageClassName());
			throw new DispatchingException(message);
		}		
	}
	
	private boolean isStateMessageTypeEqualsToExpected(Message message){
		return filter.isStateMessageValid(message);
	}
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void establishConnectionToDispatcher(){
		Message parameters = object.getParameters();
		if(parameters == null){
			String message =  String.format("%s#%d returned null instead %s.", objectClass,
					objectId, filter.getExpectedParametersMessageClassName());
			throw new DispatchingException(message);
		}else if(isParametersMessageTypeEqualsToExpected(parameters)){
			dispatcher.acceptMessage(parameters);
			setTimeWhenSentLastMessage();
			
			logger.info("{}#{} sent {} to dispatcher.", objectClass, objectId,
					filter.getExpectedParametersMessageClassName());
		}else{
			String message = String.format("%s#%d returned %s instead %s.", objectClass,
					objectId, getMessageClassName(parameters), 
					filter.getExpectedParametersMessageClassName());
			throw new DispatchingException(message);
		}
	}
	
	private boolean isParametersMessageTypeEqualsToExpected(Message message){
		return filter.isParametersMessageValid(message);
	}
}
