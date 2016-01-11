package com.epsm.electricPowerSystemModel.service;

import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epsm.electricPowerSystemModel.client.ConsumerParametersClient;
import com.epsm.electricPowerSystemModel.client.ConsumerStateClient;
import com.epsm.electricPowerSystemModel.client.PowerStationParametersClient;
import com.epsm.electricPowerSystemModel.client.PowerStationStateClient;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;

@RunWith(MockitoJUnitRunner.class)
public class OutgoingMessageServiceImplTest {
	private UnknownParameters unknownParameters;
	private UnknownState unknownState;
	private PowerStationParameters stationParameters;
	private ConsumerParametersStub consumerParameters;
	private PowerStationState powerStationState;
	private ConsumerState consumerState;
	
	@InjectMocks
	private OutgoingMessageServiceImpl service;
	
	@Mock
	private PowerStationParametersClient stationParametersClient;
	
	@Mock
	private PowerStationStateClient stationStateClient;
	
	@Mock
	private ConsumerParametersClient consumerParametersClient;
	
	@Mock
	private ConsumerStateClient consumerStateClient;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void usesStationParametersClientForSendingStationParameters(){
		stationParameters = new PowerStationParameters(1, LocalDateTime.MIN, LocalTime.MIN, 1);
		service.establishConnection(stationParameters);
		
		verify(stationParametersClient).sendStationParameters(stationParameters);
	}
	
	@Test
	public void usesStationStateClientForSendingStationState(){
		powerStationState = new PowerStationState(1, LocalDateTime.MIN, LocalTime.MIN, 1, 0);
		service.acceptState(powerStationState);
		
		verify(stationStateClient).sendPowerStationState(powerStationState);
	}
	
	@Test
	public void usesConsumerParametersClientForSendingConsumerParameters(){
		consumerParameters = new ConsumerParametersStub(1, LocalDateTime.MIN, LocalTime.MIN);
		service.establishConnection(consumerParameters);
		
		verify(consumerParametersClient).sendConsumerParameters(consumerParameters);
	}
	
	@Test
	public void usesConsumerStateClientForSendingConsumerState(){
		consumerState = new ConsumerState(1, LocalDateTime.MIN, LocalTime.MIN, 0);
		service.acceptState(consumerState);
		
		verify(consumerStateClient).sendConsumerState(consumerState);
	}
	
	@Test
	public void exceptionIfParametersTypeIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("OutgoingMessageServiceImpl establishConnection(...):"
	    		+ " parameters must not be null.");
	    
	    service.establishConnection(null);
	}
	
	@Test
	public void exceptionIfSateTypeIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("OutgoingMessageServiceImpl acceptState(...):"
	    		+ " type must not be null.");
		
		service.acceptState(null);
	}
	
	@Test
	public void exceptionIfParametersTypeIsUnsupported(){
		expectedEx.expectMessage("OutgoingMessageServiceImpl establishConnection(...):"
	    		+ " UnknownParameters is unsupported");
		
		unknownParameters = new UnknownParameters();
		service.establishConnection(unknownParameters);
	}
	
	private class UnknownParameters extends Parameters{
		public UnknownParameters() {
			super(1, LocalDateTime.MIN, LocalTime.MIN);
		}

		@Override
		public String toString() {
			return null;
		}
	}
	
	@Test
	public void exceptionIfSateTypeIsUnsupported(){
		expectedEx.expectMessage("OutgoingMessageServiceImpl acceptState(...):"
	    		+ " UnknownState is unsupported");
		
		unknownState = new UnknownState();
		service.acceptState(unknownState);
	}
	
	private class UnknownState extends State{
		public UnknownState() {
			super(1, LocalDateTime.MIN, LocalTime.MIN);
		}

		@Override
		public String toString() {
			return null;
		}
	}
}
