package test.java.com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
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
	public void isSenderGetsReportFromSource() throws InterruptedException{
		startSendReports();
		doPauseWhileTaskWillBePrepared();
		hasReportBeenRequestedFromSource();
	}
	
	private void doPauseWhileTaskWillBePrepared() throws InterruptedException{
		Thread.sleep(2000);//too many because test will be failed under maven test otherwise.
	}
	
	private void startSendReports(){
		sender.sendReports();
	}
	
	private void hasReportBeenRequestedFromSource(){
		verify(source, atLeastOnce()).getState();
	}
	
	@Test
	public void isSenderSendsReportsToDispatcher() throws InterruptedException{
		startSendReports();
		doPauseWhileTaskWillBePrepared();
		hasReportBeenSentToDispatcher();
	}
	
	private void hasReportBeenSentToDispatcher(){
		verify(dispatcher, atLeastOnce()).acceptReport(any());
	}
	
	@Test
	public void isSenderPassesRightObjectToDispatcher() throws InterruptedException{
		startSendReports();
		doPauseWhileTaskWillBePrepared();
		captureSentToDispatcherReport();
		
		Assert.assertEquals(reportFromSource, capturedReport);
	}
	
	private void captureSentToDispatcherReport(){
		verify(dispatcher, atLeastOnce()).acceptReport(reportCaptor.capture());
		capturedReport = reportCaptor.getValue();
	}
}