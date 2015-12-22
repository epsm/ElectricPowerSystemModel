package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class ReportSender {
	private Dispatcher dispatcher;
	private ReportSenderSource source;
	private Timer reportTimer;
	private SendReportTask task = new SendReportTask();
	private String sourceType;
	private final int DELAY_BEFORE_SENDING_REPORTS = 0;
	private Logger logger = (Logger) LoggerFactory.getLogger(ReportSender.class);
	
	public ReportSender(ReportSenderSource source) {
		this.source = source;
		source.setReportSender(this);
		
		determineSourceType();
	}

	private void determineSourceType(){
		if(source instanceof MainControlPanel){
			sourceType = "Station";
		}else if(source instanceof PowerConsumer){
			sourceType = "Consumer";
		}else if(source instanceof ElectricPowerSystemSimulation){
			sourceType = "Simulation";
		}else{
			throw new PowerStationException("ReportSender: unknown source type.");
		}
	}
	
	public void sendReports(){
		createTmier();
		startSending();
	}
	
	private void createTmier(){
		reportTimer = new Timer();
		logger.info(sourceType + " will be sending reports to dispatcher.");
	}
	
	private void startSending(){
		reportTimer.schedule(task, DELAY_BEFORE_SENDING_REPORTS,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}

	public void setSource(ReportSenderSource source) {
		this.source = source;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	private class SendReportTask extends TimerTask{
		private Report report;
		
		@Override
		public void run(){
			setThreadName();
			getReportFromSource();
			sendReportToDispatcher();
			
			logger.info(sourceType + " sent report to dispatcher: {}", report);
		}
		
		private void setThreadName(){
			Thread.currentThread().setName(sourceType + " ReportSender");
		}
		
		private void getReportFromSource(){
			report = source.getReport();
		}
		
		private void sendReportToDispatcher(){
			dispatcher.acceptReport(report);
		}
	}
}
