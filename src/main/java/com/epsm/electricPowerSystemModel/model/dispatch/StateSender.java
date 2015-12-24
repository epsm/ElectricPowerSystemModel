package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class StateSender {
	private Dispatcher dispatcher;
	private StateSenderSource source;
	private Timer sendingTimer;
	private SendStateTask task;
	private String sourceType;
	private Logger logger;
	private final int DELAY_BEFORE_SENDING_REPORTS = 0;	
	
	public StateSender(StateSenderSource source) {
		this.source = source;
		source.setStateSender(this);
		task = new SendStateTask();
		logger = LoggerFactory.getLogger(StateSender.class);
		determineSourceType();
	}

	private void determineSourceType(){
		sourceType = source.getClass().getSimpleName();
	}
	
	public void sendReports(){
		createTimer();
		startSending();
	}
	
	private void createTimer(){
		sendingTimer = new Timer();
		logger.info(sourceType + " will be sending reports to dispatcher.");
	}
	
	private void startSending(){
		sendingTimer.schedule(task, DELAY_BEFORE_SENDING_REPORTS,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	private class SendStateTask extends TimerTask{
		private PowerObjectState state;
		
		@Override
		public void run(){
			setThreadName();
			getStateFromSource();
			sendStateToDispatcher();
			
			logger.info(sourceType + ": {}", state);
		}
		
		private void setThreadName(){
			Thread.currentThread().setName("sender");
		}
		
		private void getStateFromSource(){
			state = source.getState();
		}
		
		private void sendStateToDispatcher(){
			dispatcher.acceptReport(state);
		}
	}
}
