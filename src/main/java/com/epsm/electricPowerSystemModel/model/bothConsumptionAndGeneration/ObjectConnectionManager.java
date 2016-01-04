package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ObjectConnectionManager{
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private MessageFilter filter;
	private volatile LocalDateTime timeWhenRecievedLastCommand;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private long objectId;
	private String objectClass;
	private Logger logger;

	public ObjectConnectionManager(TimeService timeService,	Dispatcher dispatcher, 
			PowerObject object){
		
		if(timeService == null){
			String message = String.format("ObjectConnectionManager constructor: timeService "
					+ "must not be null.");
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			String message = "ObjectConnectionManager constructor: dispatcher must not be null.";
			throw new IllegalArgumentException(message);
		}else if(object == null){
			String message = "ObjectConnectionManager constructor: PowerObject must not be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.object = object;
		objectClass = object.getClass().getSimpleName();
		filter = new MessageFilter(object.getClass());
		objectId = object.getId();
		timeWhenRecievedLastCommand = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(ObjectConnectionManager.class);
	}
	
	public void executeCommand(Command command) {
		if(command == null){
			logger.warn("ObjectConnectionManager#{} recieved null from dispatcher.", objectId);
		}else if(isCommandTypeEqualsToExpected(command)){
			setTimeWhenReceivedLastCommand();
			passCommandToObject(command);
			
			logger.info("ObjectConnectionManager#{} recieved {} from dispatcher.", 
					objectId, getMessageClassName(command));
		}else{
			logger.warn("ObjectConnectionManager#{} recieved from dispatcher wrong command class: "
					+ "expected {}, but was {}.", objectId, 
					filter.getExpectedCommandClassName(), getMessageClassName(command));
		}
	}
	
	private void passCommandToObject(Command command){
		object.executeCommand(command);
	}
	
	private String getMessageClassName(Message message){
		return message.getClass().getSimpleName();
	}
	
	private boolean isCommandTypeEqualsToExpected(Command command){
		return filter.isCommandTypeAppropriate(command);
	}
	
	private void setTimeWhenReceivedLastCommand(){
		timeWhenRecievedLastCommand = timeService.getCurrentTime();
	}
	
	public final void manageConnection(){
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
		return timeWhenRecievedLastCommand.plusSeconds(
				GlobalConstants.MAX_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECONDS)
				.isAfter(currentTime);
	}
	
	private boolean isItTimeToSentMessage(){
		return timeWhenSentLastMessage.plusSeconds(
				GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECONDS)
				.isBefore(currentTime);
	}
	
	private void sendStateToDispatcher(){
		State state = object.getState();
		
		if(state == null){
			String message =  String.format("%s#%d returned null instead %s.", objectClass,
					objectId, filter.getExpectedStateClassName());
			throw new IllegalArgumentException(message);
		}else if(isStateTypeEqualsToExpected(state)){
			dispatcher.acceptState(state);
			
			logger.info("%s#{} sent {} to dispatcher.", objectId, objectClass,
					filter.getExpectedStateClassName());
		}else{
			String message = String.format("%s#%d returned %s instead %s.", objectClass,
					objectId, getMessageClassName(state), filter.getExpectedStateClassName());
			throw new IllegalArgumentException(message);
		}		
	}
	
	private boolean isStateTypeEqualsToExpected(State state){
		return filter.isStateTypeAppropriate(state);
	}
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void establishConnectionToDispatcher(){
		Parameters parameters = object.getParameters();
		dispatcher.establishConnection(parameters);
		setTimeWhenSentLastMessage();
			
		logger.info("{}#{} sent {} to dispatcher.", objectClass, objectId,
				parameters.getClass().getSimpleName());
	}
}
