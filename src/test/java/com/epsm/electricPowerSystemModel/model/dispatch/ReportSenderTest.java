package test.java.com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSenderSource;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class ReportSenderTest{
	private ArgumentCaptor<PowerObjectState> reportCaptor;
	private Dispatcher dispatcher;
	private ReportSenderSource source;
	private ReportSender sender;
	private PowerObjectState reportFromSource;
	private PowerObjectState capturedReport;
	
	@Before
	public void initialize(){
		dispatcher = mock(Dispatcher.class);
		source = mock(ReportSenderSource.class);
		sender = new ReportSender(source); 
		sender.setDispatcher(dispatcher);
		reportFromSource = mock(PowerObjectState.class);
		reportCaptor = ArgumentCaptor.forClass(PowerObjectState.class);
		
		when(source.getState()).thenReturn(reportFromSource);
	}
	
	@Test
	public void isSenderGetsReportFromSourceEverySecond() throws InterruptedException{
		startSendReports();
		doPauseEnoughtForTwoRequest();
		hasReportBeenRequestedFromSourceTwice();
	}
	
	private void startSendReports(){
		sender.sendReports();
	}
	
	private void doPauseEnoughtForTwoRequest() throws InterruptedException{
		Thread.sleep((long)(GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS * 1.3));
	}
	
	private void hasReportBeenRequestedFromSourceTwice(){
		verify(source, times(2)).getState();
	}
	
	@Test
	public void isSenderSendsReportsToDispatcherEverySecond() throws InterruptedException{
		startSendReports();
		doPauseEnoughtForTwoRequest();
		hasReportBeenSentToDispatcherTwice();
	}
	
	private void hasReportBeenSentToDispatcherTwice(){
		verify(dispatcher, times(2)).acceptReport(any());
	}
	
	@Test
	public void isSenderPassesRightObjectToDispatcher() throws InterruptedException{
		startSendReports();
		doPauseWhileTaskWillBePrepared();
		captureSentToDispatcherReport();
		
		Assert.assertEquals(reportFromSource, capturedReport);
	}
	
	private void doPauseWhileTaskWillBePrepared() throws InterruptedException{
		Thread.sleep(200);
	}
	
	private void captureSentToDispatcherReport(){
		verify(dispatcher).acceptReport(reportCaptor.capture());
		capturedReport = reportCaptor.getValue();
	}
}