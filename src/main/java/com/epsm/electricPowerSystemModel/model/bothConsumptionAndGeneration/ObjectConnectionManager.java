package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ObjectConnectionManager{
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private MessageFilter filter;
	private volatile LocalDateTime timeWhenRecievedLastCommand;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
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
		filter = new MessageFilter(object.getClass());
		timeWhenRecievedLastCommand = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(ObjectConnectionManager.class);
	}
	
	public void executeCommand(Command command){
		if(command == null){
			logger.warn("{} recieved null from dispatcher.", object);
		}else if(isCommandTypeEqualsToExpected(command)){
			setTimeWhenReceivedLastCommand();
			passCommandToObject(command);
			
			logger.info("{} recieved {} from dispatcher.", 
					object, getMessageClassName(command));
		}else{
			logger.warn("{} recieved from dispatcher wrong command class: expected {},"
					+ " but was {}.",
					object, filter.getExpectedCommandClassName(),getMessageClassName(command));
		}
	}
	
	private void passCommandToObject(Command command){
		object.performDispatcheCommand(command);
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
		logger.debug("{}, last sent time: {}, last recieced time: {}, conn.active: {}",
				object, timeWhenSentLastMessage.toLocalTime(),
				timeWhenRecievedLastCommand.toLocalTime(), isConnectionWithDispatcherActive());
		if(isItTimeToSentMessage()){
			if(isConnectionWithDispatcherActive()){
				sendStateToDispatcher();
				setTimeWhenSentLastMessage();
			}else{
				establishConnectionToDispatcher();
				setTimeWhenSentLastMessage();
			}
		}
	}
	
	private void getCurrentTime(){
		currentTime = timeService.getCurrentTime();
	}
	
	private boolean isConnectionWithDispatcherActive(){
		return timeWhenRecievedLastCommand.plusSeconds(
				Constants.CONNECTION_TIMEOUT_IN_SECONDS)
				.isAfter(currentTime);
	}
	
	private boolean isItTimeToSentMessage(){
		return timeWhenSentLastMessage.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS)
				.isBefore(currentTime);
	}
	
	private void sendStateToDispatcher(){
		State state = object.getState();
		
		if(state == null){
			String message =  String.format("%s returned null instead %s.", object,
					filter.getExpectedStateClassName());
			throw new IllegalArgumentException(message);
		}else if(isStateTypeEqualsToExpected(state)){
			dispatcher.acceptState(state);
			
			logger.info("{} sent {} to dispatcher.", object,
					filter.getExpectedStateClassName());
		}else{
			String message = String.format("%s returned %s instead %s.", object,
					getMessageClassName(state), filter.getExpectedStateClassName());
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
			
		logger.info("{} sent {} to dispatcher.", object, parameters.getClass().getSimpleName());
	}
}
