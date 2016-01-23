package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class ObjectConnectionManager{
	private PowerObject object;
	private TimeService timeService;
	private MessageFilter filter;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private boolean registeredWithDispatcher;
	private Logger logger;

	public ObjectConnectionManager(TimeService timeService,	Dispatcher dispatcher, 
			PowerObject object){
		
		logger = LoggerFactory.getLogger(ObjectConnectionManager.class);
		
		if(timeService == null){
			String message = String.format("ObjectConnectionManager constructor: timeService "
					+ "must not be null.");
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(object == null){
			String message = "ObjectConnectionManager constructor: PowerObject must not be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.timeService = timeService;
		this.object = object;
		filter = new MessageFilter(object.getClass());
		timeWhenSentLastMessage = LocalDateTime.MIN;
		statesToSend = Collections.synchronizedList(new ArrayList<State>());
		unnecessaryStates = new ArrayList<State>();
		
	}
	
	public void executeCommand(Command command){
		if(command == null){
			logger.warn("{} recieved null from dispatcher.", object);
		}else if(isCommandTypeEqualsToExpected(command)){
			passCommandToObject(command);
			logger.info("{} recieved {} from dispatcher.", 
					object, getMessageClassName(command));
		}else{
			logger.warn("{} recieved from dispatcher wrong command class: expected {}, but was {}.",
					object, filter.getExpectedCommandClassName(),getMessageClassName(command));
		}
	}
	
	private boolean isCommandTypeEqualsToExpected(Command command){
		return filter.isCommandTypeAppropriate(command);
	}
	
	private void passCommandToObject(Command command){
		object.performDispatcheCommand(command);
	}
	
	private String getMessageClassName(Message message){
		return message.getClass().getSimpleName();
	}
	
	public final void manageConnection(){
		getCurrentTime();
		logger.debug("{}, last sent time: {}, registered: {}",
				object, timeWhenSentLastMessage.toLocalTime(), registeredWithDispatcher);
		
		if(isItTimeToSentMessage()){
			if(registeredWithDispatcher){
				sendStatesToDispatcher();
				setTimeWhenSentLastMessage();
			}else{
				registerWithDispatcher();
				setTimeWhenSentLastMessage();
			}
		}
		
		removeOldStates();
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
		sendStates();
		deleteUnnecessaryStatesFromQueue();
	}
	
	private void sendStates(){
		for(State state: statesToSend){
			boolean sent = dispatcher.acceptState(state);
			
			if(sent){
				unnecessaryStates.add(state);
				logger.debug("Deleted: {} from queue as it sent.",state);
			}
		}
	}
	
	private void deleteUnnecessaryStatesFromQueue(){
		statesToSend.removeAll(unnecessaryStates);
		clearStatesToDeleteList();
	}
	
	private void clearStatesToDeleteList(){
		unnecessaryStates.clear();
	}
	
	private void setTimeWhenSentLastMessage(){
		timeWhenSentLastMessage = currentTime;
	}
	
	private void registerWithDispatcher(){
		Parameters parameters = object.getParameters();
		registeredWithDispatcher = dispatcher.registerObject(parameters);
		logger.info("{} sent {} to dispatcher.", object, parameters.getClass().getSimpleName());
	}
	
	private void removeOldStates(){
		findOldStates();
		deleteUnnecessaryStatesFromQueue();
	}
	
	private void findOldStates(){
		for(State state: statesToSend){
			if(isMessageTooOld(state)){
				unnecessaryStates.add(state);
				logger.warn("Deleted: {} as it it in queue more than {} minutes.",
						state, Constants.PAUSE_BEFORE_DELETE_UNSENT_MESSAGES_IN_MINUTES);
			}
		}
	}
	
	private boolean isMessageTooOld(State state){
		return state.getRealTimeStamp().plusMinutes(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS)
				.isBefore(timeService.getCurrentDateTime());
	}
	
	public void acceptState(State state){
		if(isStateTypeEqualsToExpected(state)){
			prepareStateToSending(state);	
			logger.info("{} passed.", object, filter.getExpectedStateClassName());
		}else{
			String message = String.format("%s passed %s instead %s.", object,
					getMessageClassName(state), filter.getExpectedStateClassName());
			throw new IllegalArgumentException(message);
		}
	}
	
	private boolean isStateTypeEqualsToExpected(State state){
		return filter.isStateTypeAppropriate(state);
	}
	
	private void prepareStateToSending(State state){
		if(!registeredWithDispatcher){
			return;
		}
		
		if(isMessageQueueOverfull()){
			logger.warn("Message queue overfull. cant accept {}.", state);
			return;
		}
		
		statesToSend.add(state);
	}
	
	private boolean isMessageQueueOverfull(){
		return statesToSend.size() > Constants.MAX_MESSAGE_QUEUE_SIZE;
	}
}
