package com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorState;
import com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationState;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerStationTest{
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	private PowerStationParameters stationParameters;
	private PowerStationState stationState;
	private PowerStation station;
	private Generator generator_1;
	private Generator generator_2;
	private Generator generator_3;
	private StaticRegulator staticRegulator_1;
	private StaticRegulator staticRegulator_2;
	private StaticRegulator staticRegulator_3;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private LocalTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalTime.NOON;
	private LocalDateTime CONSTANT_REAL_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	private final float FIRST_GENERATOR_RQUIRED_POWER = 20;
	private final float SECOND_GENERATOR_RQUIRED_POWER = 50;
	private final float THIRD_GENERATOR_RQUIRED_POWER = 100;
	private final float FIRST_GENERATOR_NOMINAL_POWER = 200;
	private final float SECOND_GENERATOR_NOMINAL_POWER = 300;
	private final float THIRD_GENERATOR_NOMINAL_POWER = 400;
	private final float FIRST_GENERATOR_MIN_POWER = 5;
	private final long POWER_STATION_ID = 4458;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstants.STANDART_FREQUENCY);
		when(simulation.getTimeInSimulation()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		
		timeService = mock(TimeService.class);
		when(timeService.getCurrentTime()).thenReturn(CONSTANT_REAL_TIME);
		
		dispatcher = mock(Dispatcher.class);
		
		station = new PowerStation(simulation, timeService, dispatcher);
	}
	
	void prepareAndInstallFirstGenerator(){
		generator_1 = new Generator(simulation, 1);
		staticRegulator_1 = new StaticRegulator(simulation, generator_1);
		
		generator_1.setStaticRegulator(staticRegulator_1);
		generator_1.setMinimalPowerInMW(FIRST_GENERATOR_MIN_POWER);
		generator_1.setNominalPowerInMW(FIRST_GENERATOR_NOMINAL_POWER);
		generator_1.setPowerAtRequiredFrequency(FIRST_GENERATOR_RQUIRED_POWER);
		
		station.addGenerator(generator_1);
	}
	
	void prepareAndInstallSecondAndThirdGenerators(){
		generator_2 = new Generator(simulation, 2);
		generator_3 = new Generator(simulation, 3);
		staticRegulator_2 = mock(StaticRegulator.class);
		staticRegulator_3 = mock(StaticRegulator.class);
		
		generator_2.setStaticRegulator(staticRegulator_2);
		generator_3.setStaticRegulator(staticRegulator_3);
		generator_2.setNominalPowerInMW(SECOND_GENERATOR_NOMINAL_POWER);
		generator_3.setNominalPowerInMW(THIRD_GENERATOR_NOMINAL_POWER);
		generator_2.setPowerAtRequiredFrequency(SECOND_GENERATOR_RQUIRED_POWER);
		generator_3.setPowerAtRequiredFrequency(THIRD_GENERATOR_RQUIRED_POWER);

		when(staticRegulator_2.getGeneratorPowerInMW()).thenReturn(SECOND_GENERATOR_RQUIRED_POWER);
		when(staticRegulator_3.getGeneratorPowerInMW()).thenReturn(THIRD_GENERATOR_RQUIRED_POWER);
		
		station.addGenerator(generator_2);
		station.addGenerator(generator_3);
	}
	
	@Test
	public void GenerationOfElectricStationEqualsToSumOfAllTurnedOnGenerators(){
		prepareAndInstallFirstGenerator();
		prepareAndInstallSecondAndThirdGenerators();
		turnOnFirstAndSecondGeneratorsAndTurnOffThird();
		float sumOfPowerTwoTurnedOnGenerators = FIRST_GENERATOR_RQUIRED_POWER + SECOND_GENERATOR_RQUIRED_POWER;
		float stationGeneration = station.calculateGenerationInMW();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, stationGeneration, 0);
	}
	
	private void turnOnFirstAndSecondGeneratorsAndTurnOffThird(){
		generator_1.turnOnGenerator();
		generator_2.turnOnGenerator();
		generator_3.turnOffGenerator();
	}
	
	@Test
	public void powerStationParametersContainsAsManyGeneratorsAsPresentInThePowerStation(){
		prepareAndInstallFirstGenerator();
		prepareAndInstallSecondAndThirdGenerators();
		getPowerStationParameters();
		int numberOfGeneratorsInParameters = stationParameters.getQuantityOfInclusions();
		
		Assert.assertEquals(3, numberOfGeneratorsInParameters);
	}
	
	private void getPowerStationParameters(){
		stationParameters = station.getPowerStationParameters();
	}
	
	@Test
	public void powerStationParametersContainsCorrectData(){
		prepareAndInstallFirstGenerator();
		getPowerStationParameters();
		compareDataFromParametersWithReal();
	}
	
	private void compareDataFromParametersWithReal(){
		GeneratorParameters parameters = stationParameters.getGeneratorParameters(1);
		int generatorNumber = parameters.getInclusionNumber();
		float minimalPower = parameters.getMinimalTechnologyPower();
		float nominalPower = parameters.getNominalPowerInMW();
		
		Assert.assertEquals(1, generatorNumber);
		Assert.assertEquals(FIRST_GENERATOR_MIN_POWER, minimalPower, 0);
		Assert.assertEquals(FIRST_GENERATOR_NOMINAL_POWER, nominalPower, 0);
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
			Integer generatorNumber = generator.getNumber();
			
			Assert.assertEquals(generatorNumber, requestedGeneratorNumber);
		}
	}
	
	@Test
	public void exceptionIfTryToAddNullInsteadGenerator(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Generator must not be null.");
		
		station.addGenerator(null);
	}
	
	@Test
	public void exceptionIfTryToAddGeneratorWithTheSameNumberAsPreviouslyInstalled(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Generator with number 1 already installed.");
		
		prepareAndInstallFirstGenerator();
		prepareAndInstallFirstGenerator();
	}
	
	@Test
	public void exceptionIfMainControlPanelIsNullWhenCalculateGenerationInMWCalled(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Can't calculate generation with null control panel.");
		
	    station.calculateGenerationInMW();
	}
	
	@Test
	public void powerStationStateContainsCorrectData(){
		prepareAndInstallSecondAndThirdGenerators();
		turnOnSecondAndThirdGenerators();
		calculateOneStepInSimulation();
		getStation();
		verifyStationState();
	}

	private void turnOnSecondAndThirdGenerators(){
		generator_2.turnOnGenerator();
		generator_3.turnOnGenerator();
	}
	
	private void calculateOneStepInSimulation(){
		station.calculateGenerationInMW();
	}
	
	private void getStation(){
		stationState = station.getState();
	}
	
	private void verifyStationState(){
		int secondtGeneratorNumber = 0;
		float secondGeneratorGeneration = 0;
		int thirdGeneratorNumber = 0;
		float thirdGeneratorGeneration = 0;
		
		for(Integer number: stationState.getInclusionsNumbers()){
			GeneratorState generatorStateReport = stationState.getGeneratorState(number);
			
			if(generatorStateReport.getInclusionNumber() == 2){
				secondtGeneratorNumber = generatorStateReport.getInclusionNumber();
				secondGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}else if(generatorStateReport.getInclusionNumber() == 3){
				thirdGeneratorNumber = generatorStateReport.getInclusionNumber();
				thirdGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}
		}
		
		Assert.assertEquals(2, stationState.getInclusionsNumbers().size());
		Assert.assertEquals(POWER_STATION_ID, stationState.getPowerObjectId());
		Assert.assertEquals(CONSTANT_TIME_IN_MOCK_SIMULATION, stationState.getSimulationTimeStamp());
		Assert.assertEquals(CONSTANT_REAL_TIME, stationState.getRealTimeStamp());
		Assert.assertEquals(2, secondtGeneratorNumber);
		Assert.assertEquals(3, thirdGeneratorNumber);
		Assert.assertEquals(SECOND_GENERATOR_RQUIRED_POWER, secondGeneratorGeneration, 0);
		Assert.assertEquals(THIRD_GENERATOR_RQUIRED_POWER, thirdGeneratorGeneration, 0);
	}
}
