package com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorState;
import com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationState;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;

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
	private LocalTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalTime.NOON;
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
		station = new PowerStation();
		controlPanel = mock(MainControlPanel.class);
		
		station.setSimulation(simulation);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstants.STANDART_FREQUENCY);
		when(simulation.getTimeInSimulation()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		when(controlPanel.getId()).thenReturn(POWER_STATION_ID);
		
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
		setControlPanelToPowerStation();
		prepareAndInstallFirstGenerator();
		prepareAndInstallSecondAndThirdGenerators();
		turnOnFirstAndSecondGeneratorsAndTurnOffThird();
		float sumOfPowerTwoTurnedOnGenerators = FIRST_GENERATOR_RQUIRED_POWER + SECOND_GENERATOR_RQUIRED_POWER;
		float stationGeneration = station.calculateGenerationInMW();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, stationGeneration, 0);
	}
	
	public void setControlPanelToPowerStation(){
		station.setMainControlPanel(controlPanel);
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
		int numberOfGeneratorsInParameters = stationParameters.getGeneratorsNumbers().size();
		
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
		int generatorNumber = parameters.getGeneratorNumber();
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
	public void exceptionWhenSetMainControlPaneCalledWithNullParameter(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Can't add null control panel.");
		
	    station.setMainControlPanel(null);
	}
	
	@Test
	public void powerStationStateContainsCorrectData(){
		setControlPanelToPowerStation();
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
		
		for(GeneratorState generatorStateReport: stationState.getGeneratorsStates()){
			if(generatorStateReport.getGeneratorNumber() == 2){
				secondtGeneratorNumber = generatorStateReport.getGeneratorNumber();
				secondGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}else if(generatorStateReport.getGeneratorNumber() == 3){
				thirdGeneratorNumber = generatorStateReport.getGeneratorNumber();
				thirdGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}
		}
		
		Assert.assertEquals(2, stationState.getGeneratorsStates().size());
		Assert.assertEquals(POWER_STATION_ID, stationState.getPowerObjectId());
		Assert.assertEquals(CONSTANT_TIME_IN_MOCK_SIMULATION, stationState.getTimeStamp());
		Assert.assertEquals(2, secondtGeneratorNumber);
		Assert.assertEquals(3, thirdGeneratorNumber);
		Assert.assertEquals(SECOND_GENERATOR_RQUIRED_POWER, secondGeneratorGeneration, 0);
		Assert.assertEquals(THIRD_GENERATOR_RQUIRED_POWER, thirdGeneratorGeneration, 0);
	}
}
