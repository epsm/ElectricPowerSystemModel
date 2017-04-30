package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class PowerObjectStateManager<T extends Parameters, E extends State> {

	protected Dispatcher dispatcher;
	private PowerObject<T, E> powerObject;
	private List<E> statesStorage;
	private List<E> sentStates;
	private TimeService timeService;
	private LocalDateTime timeWhenSentLastMessage;
	private LocalDateTime currentTime;
	private boolean registeredWithDispatcher;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PowerObjectStateManager(TimeService timeService, Dispatcher dispatcher) {
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		statesStorage = new CopyOnWriteArrayList<>();
		sentStates = new ArrayList<>();
		timeWhenSentLastMessage = LocalDateTime.MIN;
	}
	
	public void manageStates(){
		currentTime = timeService.getCurrentDateTime();

		logger.debug("ManageMessage(): {}, last sent time: {}, registered: {}",
				powerObject, timeWhenSentLastMessage.toLocalTime(), registeredWithDispatcher);
		
		if(isItTimeToSentMessage()){
			if(registeredWithDispatcher){
				sendStatesToDispatcher();
			}else{
				registerWithDispatcher();
			}

			timeWhenSentLastMessage = currentTime;
		}
	}

	//TODO verify isBefore() or isAfter()
	private boolean isItTimeToSentMessage(){
		return timeWhenSentLastMessage.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS)
				.isBefore(currentTime);
	}
	
	private void sendStatesToDispatcher(){
		sendAndRememberSentMessages();
		deleteSentStates();
		manageStorageLoad();
	}
	
	private void sendAndRememberSentMessages(){
		List<E> packToSand = statesStorage.stream().collect(Collectors.toList());

		if(doSend(packToSand)) {
			sentStates.addAll(packToSand);
			logger.info("Sent: {} to dispatcher.",packToSand);
		} else {
			registeredWithDispatcher = false;
			logger.warn("States {} were not sent to dispatcher.", packToSand);
		}
	}
	
	private void deleteSentStates(){
		statesStorage.removeAll(sentStates);
		sentStates.clear();
	}

	private void manageStorageLoad() {
		if(statesStorage.size() > Constants.STATES_STORAGE_MAX_CAPACITY) {
			statesStorage.clear();
		}
	}
	
	private void registerWithDispatcher() {
		registeredWithDispatcher = doRegister(powerObject.getParameters());
		logger.info("Sent: {} to dispatcher", powerObject.getParameters());
	}
	
	public void acceptState(E state){
		statesStorage.add(state);
		logger.debug("Accepted: {}.", state);
	}

	public void setPowerObject(PowerObject<T, E> powerObject) {
		this.powerObject = powerObject;
	}

	protected abstract boolean doSend(List<E> states);
	protected abstract boolean doRegister(T parameters);
}
