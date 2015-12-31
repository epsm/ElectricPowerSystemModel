package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.dispatch.Message;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class PowerObject implements DispatchingObject, TimeServiceConsumer{
	protected long id;//must not bee changed after creation
	protected ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Class<? extends Message> expectedMessageType;
	private Dispatcher dispatcher;
	private volatile LocalDateTime timeWhenRecievedLastMessage;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private String thisClassName;
	private String messageClassName;
	private String stateClassName;
	private String expectedClassName;
	private Logger logger;

	public PowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, Class<? extends Message> expectedMessageType) {
		
		if(simulation == null){
			String message = "PowerObject constructor: simulation must not be null.";
			throw new DispatchingException(message);
		}else if(timeService == null){
			String message = "PowerObject constructor: timeService must not be null.";
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "PowerObject constructor: dispatcher must not be null.";
			throw new DispatchingException(message);
		}else if(expectedMessageType == null){
			String message = "PowerObject constructor: expectedMessageType must not be null.";
			throw new DispatchingException(message);
		}
		
		id = simulation.generateId();
		this.simulation = simulation;
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.expectedMessageType = expectedMessageType;
		thisClassName = this.getClass().getSimpleName();
		expectedClassName = expectedMessageType.getSimpleName();
		timeWhenRecievedLastMessage = LocalDateTime.MIN;
		timeWhenSentLastMessage = LocalDateTime.MIN;
		logger = LoggerFactory.getLogger(PowerObject.class);
		logger.info("{} was created with id {}.", thisClassName, id);
	}
	
	@Override
	public final void acceptMessage(Message message) {
		if(message == null){
			logger.warn("{} recieved null from dispatcher.", thisClassName);
			return;
		}
		
		getMessageClassName(message);
		
		if(isMessageTypeEqualsToExpected(message)){
			setLastMessageTime();
			processDispatcherMessage(message);
			
			logger.info("{} recieved {} from dispatcher.",
					thisClassName, messageClassName);
		}else{
			logger.warn("{} got from dispatcher wrong message class: {}. Expected: {}.",
					thisClassName, messageClassName, expectedClassName);
		}
	}
	
	private void getMessageClassName(Message message){
		messageClassName = message.getClass().getSimpleName();
	}
	
	private boolean isMessageTypeEqualsToExpected(Message message){
		return message.getClass().equals(expectedMessageType);
	}
	
	private void setLastMessageTime(){
		timeWhenRecievedLastMessage = timeService.getCurrentTime();
	}
	
	protected abstract void processDispatcherMessage(Message message);
	
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
		Message state = getState();

		if(state == null){
			String message = "PowerObjectState can't be null.";
			throw new DispatchingException(message);
		}
		
		dispatcher.acceptMessage(state);
		getStateClassName(state);
		logger.info("{} sent {} to dispatcher.", thisClassName, stateClassName);
	}
	
	protected abstract Message getState();
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void establishConnectionToDispatcher(){
		Message parameters = getParameters();
		
		if(parameters == null){
			String message = "PowerObjectParameters can't be null.";
			throw new DispatchingException(message);
		}
		
		dispatcher.acceptMessage(parameters);
	}
	
	public abstract Message getParameters();
	
	private void getStateClassName(Message state){
		stateClassName = state.getClass().getSimpleName();
	}
	
	public long getId(){
		return id;
	}
}
