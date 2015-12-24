package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class ReportSender {
	private Dispatcher dispatcher;
	private ReportSenderSource source;
	private Timer reportTimer;
	private SendReportTask task;
	private String sourceType;
	private Logger logger;
	private final int DELAY_BEFORE_SENDING_REPORTS = 0;
	
	private static AtomicInteger counter = new AtomicInteger(11);
	private int number;
	private long start;
	private long stop;
	
	
	public ReportSender(ReportSenderSource source) {
		this.source = source;
		source.setReportSender(this);
		task = new SendReportTask();
		logger = LoggerFactory.getLogger(ReportSender.class);
		determineSourceType();
		
		number = counter.getAndIncrement();
	}

	private void determineSourceType(){
		sourceType = source.getClass().getSimpleName();
	}
	
	public void sendReports(){
		synchronized(this){
			start = System.currentTimeMillis();
			System.out.println("scheduled " + number);
		}
		createTimer();
		startSending();
	}
	
	private void createTimer(){
		reportTimer = new Timer();
		logger.info(sourceType + " will be sending reports to dispatcher.");
	}
	
	private void startSending(){
		reportTimer.schedule(task, DELAY_BEFORE_SENDING_REPORTS,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	private class SendReportTask extends TimerTask{
		private PowerObjectState report;
		
		@Override
		public void run(){
			synchronized(this){
				stop = System.currentTimeMillis();
				System.out.println("performs " + number + ", delay: " + (stop - start));
				start = stop;
			}
			setThreadName();
			getReportFromSource();
			sendReportToDispatcher();
			
			logger.info(sourceType + ": {}", report);
		}
		
		private void setThreadName(){
			Thread.currentThread().setName("sender");
		}
		
		private void getReportFromSource(){
			report = source.getState();
		}
		
		private void sendReportToDispatcher(){
			dispatcher.acceptReport(report);
		}
	}
}
