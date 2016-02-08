package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
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
	private final long POWER_OBJECT_ID = 0;
	private final int QUANTITY_OF_GENERATORS_1 = 1;
	private final int QUANTITY_OF_GENERATORS_2 = 2;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private final float NOMINAL_POWER_IN_MW = 100;
	private final float MINIMAL_POWER_IN_MW = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	private final boolean GENERATOR_ON = true;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	private final LoadCurve NULL_CURVE = null;
	
	@Before
	public void setUp(){
		PowerStationParameters stationParameters = new PowerStationParameters(
				POWER_OBJECT_ID,REAL_TIMESTAMP, SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS_1);
		
		GeneratorParameters generatorParameters = new GeneratorParameters(
				FIRST_GENERATOR_NUMBER, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW);
		
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		
		station = new PowerStation(simulation, timeService, dispatcher, stationParameters);
		controlPanel= new MainControlPanel(simulation, station);
		
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID,
				REAL_TIMESTAMP, SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS_1);
		
		generatorSchedule = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, 
				GENERATOR_ON, ASTATIC_REGULATION_ON, NULL_CURVE);
		
		generator = spy(new Generator(simulation, FIRST_GENERATOR_NUMBER));
		
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
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID, REAL_TIMESTAMP, 
				SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS_2);
		GeneratorGenerationSchedule schedule_1 = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, NULL_CURVE);
		GeneratorGenerationSchedule schedule_2 = new GeneratorGenerationSchedule(
				SECOND_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, NULL_CURVE);
		stationSchedule.addGeneratorSchedule(schedule_1);
		stationSchedule.addGeneratorSchedule(schedule_2);
	}
}
