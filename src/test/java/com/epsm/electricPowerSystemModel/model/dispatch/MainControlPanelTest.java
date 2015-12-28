package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Mockito.mock;

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
	private Class<? extends DispatcherMessage> expectedMessageType;
	private PowerStation station;
	private PowerStationGenerationSchedule stationSchedule = new PowerStationGenerationSchedule(1);
	private GeneratorGenerationSchedule generatorSchedule;
	private Generator generator;
	
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		timeService = new TimeService();
		expectedMessageType = PowerStationGenerationSchedule.class;
		dispatcher = mock(Dispatcher.class);
		station = new PowerStation();
		controlPanel= new MainControlPanel(simulation, timeService, dispatcher,
				expectedMessageType, station);
		stationSchedule = new PowerStationGenerationSchedule(0);
		generatorSchedule = new GeneratorGenerationSchedule(1, true, true, null);
		generator = new Generator(simulation, 1);
		
		station.addGenerator(generator);
		stationSchedule.addGeneratorGenerationSchedule(generatorSchedule);
		simulation.addPowerStation(station);
	}
	
	@Test
	public void controlPanelAcceptsValidSchedule(){
		controlPanel.acceptMessage(stationSchedule);
		doNextStep();
		
		Assert.assertTrue(generator.isTurnedOn());
	}
	
	public void doNextStep(){
		controlPanel.doRealTimeDependOperation();
		simulation.calculateNextStep();
	}
	
	@Test
	public void controlPanelDoesnNotAcceptInvalidSchedule(){
		createInvalidGenerationSchedule();
		controlPanel.acceptMessage(stationSchedule);
		doNextStep();
		
		Assert.assertFalse(generator.isTurnedOn());
	}
	
	private void createInvalidGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(2);
	}
}
