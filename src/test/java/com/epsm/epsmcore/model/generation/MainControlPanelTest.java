package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainControlPanelTest{

	private Simulation simulation;
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
	private final PowerCurve NULL_CURVE = null;

	@Mock
	private PowerObjectStateManager stateManager;
	
	@Before
	public void setUp(){
		PowerStationParameters stationParameters = new PowerStationParameters(POWER_OBJECT_ID);
		
		GeneratorParameters generatorParameters = new GeneratorParameters(
				FIRST_GENERATOR_NUMBER, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW);
		
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		
		simulation = new Simulation(TestsConstants.START_DATETIME);
		
		station = new PowerStation(simulation, dispatcher, stationParameters, stateManager);
		controlPanel= new MainControlPanel(simulation, station);
		
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID);
		
		generatorSchedule = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER,
				GENERATOR_ON, ASTATIC_REGULATION_ON, NULL_CURVE);
		
		generator = spy(new Generator(simulation, FIRST_GENERATOR_NUMBER));
		
		station.addGenerator(generator);
		stationSchedule.getGeneratorSchedules().put(1, generatorSchedule);
		stationParameters.getGeneratorParameters().put(1, generatorParameters);
	}
	
	@Test
	public void controlPanelAcceptsValidSchedule(){
		controlPanel.acceptGenerationSchedule(stationSchedule);
		controlPanel.adjustGenerators();
		
		verify(generator).turnOnGenerator();
	}
	
	public void doNextStep(){
		simulation.doNextStep();
	}
	
	@Test
	public void controlPanelDoesnNotAcceptInvalidSchedule(){
		createInvalidGenerationSchedule();
		controlPanel.acceptGenerationSchedule(stationSchedule);
		doNextStep();
		
		verify(generator, never()).turnOnGenerator();
	}
	
	private void createInvalidGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID);
		GeneratorGenerationSchedule schedule_1 = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, NULL_CURVE);
		GeneratorGenerationSchedule schedule_2 = new GeneratorGenerationSchedule(
				SECOND_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, NULL_CURVE);
		stationSchedule.getGeneratorSchedules().put(1, schedule_1);
		stationSchedule.getGeneratorSchedules().put(1, schedule_2);
	}
}
