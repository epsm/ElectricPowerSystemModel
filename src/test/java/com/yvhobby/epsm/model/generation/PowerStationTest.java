package test.java.com.yvhobby.epsm.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.com.yvhobby.epsm.model.dispatch.GeneratorParameters;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class PowerStationTest{
	private ElectricPowerSystemSimulation simulation;
	private PowerStationParameters stationParameters;
	private PowerStation station;
	private Generator generator_1;
	private Generator generator_2;
	private Generator generator_3;
	private ControlUnit controlUnit_1;
	private ControlUnit controlUnit_2;
	private ControlUnit controlUnit_3;
	private final float FIRST_GENERATOR_RQUIRED_POWER = 20;
	private final float SECOND_GENERATOR_RQUIRED_POWER = 50;
	private final float THIRD_GENERATOR_RQUIRED_POWER = 100;
	private final float FIRST_GENERATOR_NOMINAL_POWER = 200;
	private final float SECOND_GENERATOR_NOMINAL_POWER = 300;
	private final float THIRD_GENERATOR_NOMINAL_POWER = 400;
	private final float FIRST_GENERATOR_MIN_POWER = 5;
	private final int POWER_STATION_NUMBER = 4458;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		station = new PowerStation(POWER_STATION_NUMBER);
		
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstatnts.STANDART_FREQUENCY);
	}
	
	void prepareAndInstallFirstGenerator(){
		generator_1 = new Generator(1);
		controlUnit_1 = new ControlUnit(simulation, generator_1);
		
		generator_1.setControlUnit(controlUnit_1);
		generator_1.setMinimalTechnologyPower(FIRST_GENERATOR_MIN_POWER);
		generator_1.setNominalPowerInMW(FIRST_GENERATOR_NOMINAL_POWER);
		generator_1.setPowerAtRequiredFrequency(FIRST_GENERATOR_RQUIRED_POWER);
	
		station.addGenerator(generator_1);
	}
	
	void prepareAndInstallSecondAndThirdGenerators(){
		generator_2 = new Generator(2);
		generator_3 = new Generator(3);
		controlUnit_2 = new ControlUnit(simulation, generator_1);
		controlUnit_3 = new ControlUnit(simulation, generator_1);
		
		generator_2.setControlUnit(controlUnit_2);
		generator_3.setControlUnit(controlUnit_3);
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
		float stationGeneration = station.getCurrentGenerationInMW();
		
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
	public void exceptionIfTryToAddNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Generator must not be null.");
		
		station.addGenerator(null);
	}
	
	@Test
	public void exceptionIfTryToAddGeneratorWithTheSameNumberAsPreviouslyInstalled(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Generator with number 1 already installed.");
		
		prepareAndInstallFirstGenerator();
		prepareAndInstallFirstGenerator();
	}
}
