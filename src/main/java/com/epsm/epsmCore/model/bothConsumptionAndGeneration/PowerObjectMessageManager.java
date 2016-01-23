package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class PowerObjectMessageManager{
	private PowerObject powerObject;
	private SendingMessageManager<State> sendingManager;
	private DispatcherRegistrationManager registrationManager;
	private TimeService timeService;
	private MessageFilter filter;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private boolean registeredWithDispatcher;
	private Logger logger;

	public PowerObjectMessageManager(TimeService timeService,	Dispatcher dispatcher, 
			PowerObject powerObject){
		
		logger = LoggerFactory.getLogger(PowerObjectMessageManager.class);
		
		if(timeService == null){
			String message = String.format("ObjectConnectionManager constructor: timeService "
					+ "must not be null.");
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(powerObject == null){
			String message = "ObjectConnectionManager constructor: powerObject must not be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.timeService = timeService;
		this.powerObject = powerObject;
		sendingManager = new SendingMessageManager<State>(dispatcher, timeService);
		registrationManager = new DispatcherRegistrationManager(dispatcher, powerObject.getParameters());
		filter = new MessageFilter(powerObject.getClass());
		timeWhenSentLastMessage = LocalDateTime.MIN;
	}
	
	public void executeCommand(Command command){
		if(command == null){
			logger.warn("{} recieved null from dispatcher.", powerObject);
		}else if(isCommandTypeEqualsToExpected(command)){
			passCommandToObject(command);
			logger.info("{} recieved {} from dispatcher.", 
					powerObject, getMessageClassName(command));
		}else{
			logger.warn("{} recieved from dispatcher wrong command class: expected {}, but was {}.",
					powerObject, filter.getExpectedCommandClassName(),getMessageClassName(command));
		}
	}
	
	private boolean isCommandTypeEqualsToExpected(Command command){
		return filter.isCommandTypeAppropriate(command);
	}
	
	private void passCommandToObject(Command command){
		powerObject.performDispatcherCommand(command);
	}
	
	private String getMessageClassName(Message message){
		return message.getClass().getSimpleName();
	}
	
	public final void manageConnection(){
		getCurrentTime();
		logger.debug("{}, last sent time: {}, registered: {}",
				powerObject, timeWhenSentLastMessage.toLocalTime(), registeredWithDispatcher);
		
		if(isItTimeToSentMessage()){
			if(registeredWithDispatcher){
				sendStatesToDispatcher();
				setTimeWhenSentLastMessage();
			}else{
				registerWithDispatcher();
				setTimeWhenSentLastMessage();
			}
		}
	}
	
	private void getCurrentTime(){
		currentTime = timeService.getCurrentDateTime();
	}
	
	private boolean isItTimeToSentMessage(){
		return timeWhenSentLastMessage.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS)
				.isBefore(currentTime);
	}
	
	private void sendStatesToDispatcher(){
		sendingManager.sendStates();
	}
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void registerWithDispatcher(){
		registeredWithDispatcher = registrationManager.registerWithDispatcher();
	}
	
	public void acceptState(State state){
		if(isStateTypeEqualsToExpected(state)){
			sendingManager.acceptMessage(state);	
			logger.debug("{} passed to SendingMessageManager.", powerObject);
		}else{
			String message = String.format("%s received instead %s.",
					getMessageClassName(state), filter.getExpectedStateClassName());
			throw new IllegalArgumentException(message);
		}
	}
	
	private boolean isStateTypeEqualsToExpected(State state){
		return filter.isStateTypeAppropriate(state);
	}
}
