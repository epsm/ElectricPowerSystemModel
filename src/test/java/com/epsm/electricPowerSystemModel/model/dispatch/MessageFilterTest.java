package com.epsm.electricPowerSystemModel.model.dispatch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class MessageFilterTest {
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerObject object;
	private MessageFilter filter;
	private Message message;
	
	@Before
	public void init(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		station = mock(PowerStation.class);
	}
	
	@Test
	public void trueIfObjectIsPowerStationAndCommandIsPowerStationGenerationSchedule(){
		createMainControlPanelAndFilter();
		createPowerStationState();
		Assert.assertTrue(filter.verifyStateMessage(message));
	}
	
	private void createMainControlPanelAndFilter(){
		object = new MainControlPanel(simulation, timeService, dispatcher, station);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createPowerStationState(){
		message = new PowerStationState(0, LocalDateTime.MIN, LocalTime.MIN, 0, 0);
	}
	
	@Ignore
	@Test
	public void trueIfObjectIsShockLoadConsumerAndStateIsConsumerState(){
		
	}
}
