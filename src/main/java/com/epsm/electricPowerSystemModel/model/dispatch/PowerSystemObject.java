package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.util.TimerTask;

import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class PowerSystemObject implements ObjectToBeDispatching{
	
	private TimeService timeService;
	private String childNameForLogging;
	private Object expectedMessageType;
	private Dispatcher dispatcher;
	private LocalDateTime lastMessageFromDispatcherTime;
	private DispatcherMessage message;
	private volatile boolean connectionWithDispatcherActive;

	public PowerSystemObject(Object expectedMessageType, String childNameForLogging) {
		this.expectedMessageType = expectedMessageType;
	}
	
	@Override
	public void acceptMessage(DispatcherMessage message) {
		this.message = message;
		
		if(isMessageTypeEqualsToExpected()){
			setConnectionActive();
			setLastMessageTime();
		}
	}
	
	private boolean isMessageTypeEqualsToExpected(){
		return message.getClass().equals(expectedMessageType.getClass());
	}
	
	private void setConnectionActive(){
		connectionWithDispatcherActive = true;
	}
	
	private void setLastMessageTime(){
		lastMessageFromDispatcherTime = timeService.getCurrentTime();
	}
	
	 
	
	
	
	
	private void connectToDispatcher(){
		dispatcher.connectToPowerObject(this);
	}
	
	private class Task extends TimerTask{

		@Override
		public void run() {
			
			
		}
	}
	
	protected abstract void processDispatcherMessage(DispatcherMessage message);
}
