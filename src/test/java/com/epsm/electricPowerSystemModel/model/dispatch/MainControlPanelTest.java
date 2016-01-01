package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.Generator;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class MainControlPanelTest{
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerStationGenerationSchedule stationSchedule;
	private GeneratorGenerationSchedule generatorSchedule;
	private Generator generator;
	
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		station = new PowerStation(simulation, timeService, dispatcher);
		controlPanel= new MainControlPanel(simulation, station);
		stationSchedule = new PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalTime.MIN, 1);
		generatorSchedule = new GeneratorGenerationSchedule(1);
		generator = new Generator(simulation, 1);
		
		station.addGenerator(generator);
		stationSchedule.addInclusion(generatorSchedule);
		simulation.addPowerStation(station);
	}
	
	@Test
	public void controlPanelAcceptsValidSchedule(){
		station.processDispatcherMessage(stationSchedule);
		doNextStep();
		
		Assert.assertTrue(generator.isTurnedOn());
	}
	
	public void doNextStep(){
		station.doRealTimeDependingOperation();
		simulation.calculateNextStep();
	}
	
	@Test
	public void controlPanelDoesnNotAcceptInvalidSchedule(){
		createInvalidGenerationSchedule();
		controlPanel.process(stationSchedule);
		doNextStep();
		
		Assert.assertFalse(generator.isTurnedOn());
	}
	
	private void createInvalidGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(2);
	}
}
