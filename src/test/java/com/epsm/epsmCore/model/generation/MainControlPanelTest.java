package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;

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
	public void setUp(){
		PowerStationParameters stationParameters 
				= new PowerStationParameters(0, LocalDateTime.MIN, LocalDateTime.MIN, 1);
		GeneratorParameters generatorParameters = new GeneratorParameters(1, 100, 0);
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		station = new PowerStation(simulation, timeService, dispatcher, stationParameters);
		controlPanel= new MainControlPanel(simulation, station);
		stationSchedule 
				= new PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalDateTime.MIN, 1);
		generatorSchedule = new GeneratorGenerationSchedule(1, true, true, null);
		generator = spy(new Generator(simulation, 1));
		
		station.addGenerator(generator);
		stationSchedule.addGeneratorSchedule(generatorSchedule);
		stationParameters.addGeneratorParameters(generatorParameters);
	}
	
	@Test
	public void controlPanelAcceptsValidSchedule(){
		controlPanel.acceptGenerationSchedule(stationSchedule);
		controlPanel.adjustGenerators();
		
		verify(generator).turnOnGenerator();
	}
	
	public void doNextStep(){
		simulation.calculateNextStep();
	}
	
	@Test
	public void controlPanelDoesnNotAcceptInvalidSchedule(){
		createInvalidGenerationSchedule();
		controlPanel.acceptGenerationSchedule(stationSchedule);
		doNextStep();
		
		verify(generator, never()).turnOnGenerator();
	}
	
	private void createInvalidGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(
				0, LocalDateTime.MIN, LocalDateTime.MIN, 2);
		GeneratorGenerationSchedule schedule_1 = new GeneratorGenerationSchedule(
				1, true, false, null);
		GeneratorGenerationSchedule schedule_2 = new GeneratorGenerationSchedule(
				2, true, false, null);
		stationSchedule.addGeneratorSchedule(schedule_1);
		stationSchedule.addGeneratorSchedule(schedule_2);
	}
}
