package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import com.epsm.electricPowerSystemModel.model.dispatch.StateSender;
import com.epsm.electricPowerSystemModel.model.dispatch.StateSenderSource;

public class ReportSenderTest{
	private ArgumentCaptor<PowerObjectState> stateCaptor;
	private Dispatcher dispatcher;
	private StateSenderSource source;
	private StateSender sender;
	private PowerObjectState stateFromSource;
	private PowerObjectState capturedReport;
	
	@Before
	public void initialize(){
		dispatcher = mock(Dispatcher.class);
		source = mock(StateSenderSource.class);
		sender = new StateSender(source); 
		sender.setDispatcher(dispatcher);
		stateFromSource = mock(PowerObjectState.class);
		stateCaptor = ArgumentCaptor.forClass(PowerObjectState.class);
		
		when(source.getState()).thenReturn(stateFromSource);
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
		
		Assert.assertEquals(stateFromSource, capturedReport);
	}
	
	private void captureSentToDispatcherReport(){
		verify(dispatcher, atLeastOnce()).acceptReport(stateCaptor.capture());
		capturedReport = stateCaptor.getValue();
	}
}