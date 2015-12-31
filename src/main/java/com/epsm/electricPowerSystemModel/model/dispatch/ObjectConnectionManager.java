package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeServiceConsumer;

public class ObjectConnectionManager implements DispatcherConnection, TimeServiceConsumer{
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private MessageFilter filter;
	private volatile LocalDateTime timeWhenRecievedLastMessage;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private String objectClassName;
	private long objectId;
	private Logger logger;

	public ObjectConnectionManager(TimeService timeService,	Dispatcher dispatcher, PowerObject object) {
		
		if(timeService == null){
			String message = "PowerObject constru5utyctor: timeService must not be null.";
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "PowerObject construyhyctor: dispatcher must not be null.";
			throw new DispatchingException(message);
		}else if(object == null){
			String message = "PowerObject constrthtuctor: expectedMessageType must not be null.";
			throw new DispatchingException(message);
		}
		
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.object = object;
		filter = new MessageFilter(object.getClass());
		objectClassName = object.getClass().getSimpleName();
		objectId = object.getId();
		timeWhenRecievedLastMessage = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(ObjectConnectionManager.class);
	}
	
	@Override
	public void acceptMessage(Message message) {
		if(message == null){
			logger.warn("{}#{} recieved null from dispatcher.", objectClassName, objectId);
		}else if(isCommandMessageTypeEqualsToExpected(message)){
			setLastMessageTime();
			object.processDispatcherMessage(message);
			
			logger.info("{}#{} recieved {} from dispatcher.", 
					objectClassName, objectId, getMessageClassName(message));
		}else{
			logger.warn("{}#{} recieved from dispatcher wrong message class: expected {}, but was {}.",
					objectClassName, objectId, getMessageClassName(message),
					filter.getExpectedCommandMessageClassName());
		}
	}
	
	private String getMessageClassName(Message message){
		return message.getClass().getName();
	}
	
	private boolean isCommandMessageTypeEqualsToExpected(Message message){
		return filter.verifyCommandMessage(message);
	}
	
	private void setLastMessageTime(){
		timeWhenRecievedLastMessage = timeService.getCurrentTime();
	}
	
	@Override
	public final void doRealTimeDependOperation(){
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
			String message =  String.format("%s#%d returned null instead %s.", objectClassName,
					objectId, filter.getExpectedStateMessageClassName());
			throw new DispatchingException(message);
		}else if(isStateMessageTypeEqualsToExpected(state)){
			dispatcher.acceptMessage(state);
			
			logger.info("{}#{} sent {} to dispatcher.", objectClassName, objectId,
					filter.getExpectedStateMessageClassName());
		}else{
			String message = String.format("%s3%d returned %s instead %s.", objectClassName, 
					objectId, getMessageClassName(state), filter.getExpectedStateMessageClassName());
			throw new DispatchingException(message);
		}		
	}
	
	private boolean isStateMessageTypeEqualsToExpected(Message message){
		return filter.verifyCommandMessage(message);
	}
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void establishConnectionToDispatcher(){
		Message parameters = object.getParameters();
		
		if(parameters == null){
			String message =  String.format("%s#%d returned null instead %s.", objectClassName,
					objectId, filter.getExpectedParametersMessageClassName());
			throw new DispatchingException(message);
		}else if(isParametersMessageTypeEqualsToExpected(parameters)){
			dispatcher.acceptMessage(parameters);
			
			logger.info("{}#{} sent {} to dispatcher.", objectClassName, objectId,
					filter.getExpectedParametersMessageClassName());
		}else{
			String message = String.format("%s#%d returned %s instead %s.", objectClassName, 
					objectId, getMessageClassName(parameters),
					filter.getExpectedParametersMessageClassName());
			throw new DispatchingException(message);
		}
		
		dispatcher.acceptMessage(parameters);
	}
	
	private boolean isParametersMessageTypeEqualsToExpected(Message message){
		return filter.verifyCommandMessage(message);
	}
}
