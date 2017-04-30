package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PowerStationTest {

	private Simulation simulation;
	private PowerStationState stationState;
	private PowerStation station;
	private Generator generator_1;
	private Generator generator_2;
	private Generator generator_3;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private LocalDateTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalDateTime.of(2000, 01, 01, 12, 00);
	private LocalDateTime CONSTANT_REAL_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	private final float FIRST_GENERATOR_RQUIRED_POWER = 20;
	private final float SECOND_GENERATOR_RQUIRED_POWER = 50;
	private final float THIRD_GENERATOR_RQUIRED_POWER = 100;
	private final float FIRST_GENERATOR_NOMINAL_POWER = 200;
	private final float SECOND_GENERATOR_NOMINAL_POWER = 300;
	private final float THIRD_GENERATOR_NOMINAL_POWER = 400;
	private final float FIRST_GENERATOR_MIN_POWER = 5;
	private final long POWER_STATION_ID = 4458;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private final int THIRD_GENERATOR_NUMBER = 3;

	@Mock
	private PowerObjectStateManager stateManager;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		PowerStationParameters parameters = new PowerStationParameters(POWER_STATION_ID);
		
		simulation = mock(Simulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(Constants.STANDART_FREQUENCY);
		when(simulation.getDateTimeInSimulation()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(CONSTANT_REAL_TIME);
		
		dispatcher = mock(Dispatcher.class);
		
		station = new PowerStation(simulation, dispatcher, parameters, stateManager);
	}
	
	void prepareAndInstallFirstGenerator(){
		generator_1 = new Generator(simulation, FIRST_GENERATOR_NUMBER);
		generator_1.setMinimalPowerInMW(FIRST_GENERATOR_MIN_POWER);
		generator_1.setNominalPowerInMW(FIRST_GENERATOR_NOMINAL_POWER);
		generator_1.setPowerAtRequiredFrequency(FIRST_GENERATOR_RQUIRED_POWER);
		
		station.addGenerator(generator_1);
	}
	
	void prepareAndInstallSecondAndThirdGenerators(){
		generator_2 = new Generator(simulation, SECOND_GENERATOR_NUMBER);
		generator_3 = new Generator(simulation, THIRD_GENERATOR_NUMBER);
		
		generator_2.setNominalPowerInMW(SECOND_GENERATOR_NOMINAL_POWER);
		generator_3.setNominalPowerInMW(THIRD_GENERATOR_NOMINAL_POWER);
		generator_2.setPowerAtRequiredFrequency(SECOND_GENERATOR_RQUIRED_POWER);
		generator_3.setPowerAtRequiredFrequency(THIRD_GENERATOR_RQUIRED_POWER);
		
		station.addGenerator(generator_2);
		station.addGenerator(generator_3);
	}
	
	@Test
	public void GenerationOfElectricStationEqualsToSumOfAllTurnedOnGenerators(){
		prepareAndInstallFirstGenerator();
		prepareAndInstallSecondAndThirdGenerators();
		turnOnFirstAndSecondGeneratorsAndTurnOffThird();
		float sumOfPowerTwoTurnedOnGenerators = FIRST_GENERATOR_RQUIRED_POWER + SECOND_GENERATOR_RQUIRED_POWER;
		float stationGeneration = station.calculatePowerBalance();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, stationGeneration, 0);
	}
	
	private void turnOnFirstAndSecondGeneratorsAndTurnOffThird(){
		generator_1.turnOnGenerator();
		generator_2.turnOnGenerator();
		generator_3.turnOffGenerator();
	}
	
	@Test
	public void generatorNumbersAreEqualToRequested(){
		prepareAndInstallFirstGenerator();
		prepareAndInstallSecondAndThirdGenerators();
		compareGeneratorNumbersWithRequested();
	}
	
	private void compareGeneratorNumbersWithRequested(){
		Collection<Integer> requestedGeneratorNumbers = station.getGeneratorsNumbers();
		
		for(Integer requestedGeneratorNumber: requestedGeneratorNumbers){
			Generator generator = station.getGenerator(requestedGeneratorNumber);
			Integer generatorNumber = generator.getGeneratorNumber();
			
			Assert.assertEquals(generatorNumber, requestedGeneratorNumber);
		}
	}
	
	@Test
	public void exceptionIfTryToAddNullInsteadGenerator(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Generator must not be null.");
		
		station.addGenerator(null);
	}
	
	@Test
	public void exceptionIfTryToAddGeneratorWithTheSameNumberAsPreviouslyInstalled(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Generator#1 already installed.");
		
		prepareAndInstallFirstGenerator();
		prepareAndInstallFirstGenerator();
	}
	
	@Test
	public void powerStationStateContainsCorrectData(){
		prepareAndInstallSecondAndThirdGenerators();
		turnOnSecondAndThirdGenerators();
		calculateOneStepInSimulation();
		getStationState();
		verifyStationState();
	}

	private void turnOnSecondAndThirdGenerators(){
		generator_2.turnOnGenerator();
		generator_3.turnOnGenerator();
	}
	
	private void calculateOneStepInSimulation(){
		station.calculatePowerBalance();
	}
	
	private void getStationState(){
		stationState = (PowerStationState) station.getState();
	}
	
	private void verifyStationState(){
		int secondtGeneratorNumber = 0;
		float secondGeneratorGeneration = 0;
		int thirdGeneratorNumber = 0;
		float thirdGeneratorGeneration = 0;
		
		for(Integer number: stationState.getStates().keySet()){
			GeneratorState generatorStateReport = stationState.getStates().get(number);
			
			if(generatorStateReport.getGeneratorNumber() == 2){
				secondtGeneratorNumber = generatorStateReport.getGeneratorNumber();
				secondGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}else if(generatorStateReport.getGeneratorNumber() == 3){
				thirdGeneratorNumber = generatorStateReport.getGeneratorNumber();
				thirdGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}
		}
		
		Assert.assertEquals(2, stationState.getStates().size());
		Assert.assertEquals(POWER_STATION_ID, stationState.getPowerObjectId());
		Assert.assertEquals(CONSTANT_TIME_IN_MOCK_SIMULATION, stationState.getSimulationTimeStamp());
		Assert.assertEquals(2, secondtGeneratorNumber);
		Assert.assertEquals(3, thirdGeneratorNumber);
		Assert.assertEquals(SECOND_GENERATOR_RQUIRED_POWER, secondGeneratorGeneration, 0);
		Assert.assertEquals(THIRD_GENERATOR_RQUIRED_POWER, thirdGeneratorGeneration, 0);
	}
}
