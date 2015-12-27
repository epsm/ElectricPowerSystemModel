package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class PowerSystemObject implements DispatchingObject{
	
	private TimeService timeService;
	private Class<? extends DispatcherMessage> expectedMessageType;
	private Dispatcher dispatcher;
	private LocalDateTime timeWhenRecievedLastMessage;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private String thisClassName;
	private String messageClassName;
	private String stateClassName;
	private Logger logger;

	public PowerSystemObject(TimeService timeService, Dispatcher dispatcher,
			Class<? extends DispatcherMessage> expectedMessageType) {
		
		if(timeService == null){
			String message = "PowerSystem object constructor: timeService must not be null.";
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "PowerSystem object constructor: dispatcher must not be null.";
			throw new DispatchingException(message);
		}else if(expectedMessageType == null){
			String message = "PowerSystem object constructor: expectedMessageType must not be null.";
			throw new DispatchingException(message);
		}
		
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.expectedMessageType = expectedMessageType;
		thisClassName = this.getClass().getSimpleName();
		timeWhenRecievedLastMessage = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(PowerSystemObject.class);
	}
	
	@Override
	public final void acceptMessage(DispatcherMessage message) {
		if(message == null){
			logger.warn("{} recieved null from dispatcher",	thisClassName);
			return;
		}

		if(isMessageTypeEqualsToExpected(message)){
			setLastMessageTime();
			processDispatcherMessage(message);
			getMessageClassName(message);
			
			logger.info("{} recieved {} from dispatcher",
					thisClassName, messageClassName);
		}else{
			logger.warn("{} recieved {} from dispatcher",
					thisClassName, messageClassName);
		}
	}
	
	private boolean isMessageTypeEqualsToExpected(DispatcherMessage message){
		return message.getClass().equals(expectedMessageType);
	}
	
	private void setLastMessageTime(){
		timeWhenRecievedLastMessage = timeService.getCurrentTime();
	}
	
	protected abstract void processDispatcherMessage(DispatcherMessage message);
	
	@Override
	public final void interactWithDisparcher(){
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
		PowerObjectState state = getState();
		
		if(state == null){
			String message = "PowerObjectState must not be null.";
			throw new DispatchingException(message);
		}
		
		dispatcher.acceptReport(state);
		getStateClassName(state);
		logger.info("{} sent {} from dispatcher", thisClassName, stateClassName);
	}
	
	protected abstract PowerObjectState getState();
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void establishConnectionToDispatcher(){
		dispatcher.connectToPowerObject(this);
	}
	
	private void getMessageClassName(DispatcherMessage message){
		message.getClass().getSimpleName();
	}
	
	private void getStateClassName(PowerObjectState state){
		state.getClass().getSimpleName();
	}
}
