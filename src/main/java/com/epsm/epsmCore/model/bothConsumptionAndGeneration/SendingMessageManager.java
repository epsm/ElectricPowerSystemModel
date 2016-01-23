package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class SendingMessageManager<T extends Message> {
	private Dispatcher dispatcher;
	private TimeService timeService;
	private List<T> statesToSend;
	private ArrayList<T> unnecessaryStates;
	private Logger logger;
	
	public SendingMessageManager(Dispatcher dispatcher, TimeService timeService) {
		logger = LoggerFactory.getLogger(SendingMessageManager.class);
		
		if(dispatcher == null){
			String message = "SendingMessageManager constructor: dispatcher must not be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(timeService == null){
			String message = String.format("SendingMessageManager constructor: timeService "
					+ "must not be null.");
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.dispatcher = dispatcher;
		this.timeService = timeService;
		statesToSend = Collections.synchronizedList(new ArrayList<T>());
		unnecessaryStates = new ArrayList<T>();
	}
	
	public void sendStates(){
		for(T state: statesToSend){
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
	
	public void acceptMessage(T message){
		//TODO write body
	}
}
