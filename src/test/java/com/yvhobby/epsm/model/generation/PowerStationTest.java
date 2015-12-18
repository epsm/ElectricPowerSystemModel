package test.java.com.yvhobby.epsm.model.generation;

import org.junit.*;

import static org.mockito.Mockito.*;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class PowerStationTest{
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station;
	private Generator generator_1;
	private Generator generator_2;
	private Generator generator_3;
	private ControlUnit controlUnit_1;
	private ControlUnit controlUnit_2;
	private ControlUnit controlUnit_3;
	private final float FIRST_GENERATOR_POWER = 20;
	private final float SECOND_GENERATOR_POWER = 50;
	private final float THIRD_GENERATOR_POWER = 100;
	
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		station = new PowerStation();
		generator_1 = new Generator();
		generator_2 = new Generator();
		generator_3 = new Generator();
		controlUnit_1 = new ControlUnit(simulation, generator_1);
		controlUnit_2 = new ControlUnit(simulation, generator_1);
		controlUnit_3 = new ControlUnit(simulation, generator_1);
		
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstatnts.STANDART_FREQUENCY);
		
		controlUnit_1.setPowerAtRequiredFrequency(FIRST_GENERATOR_POWER);
		controlUnit_2.setPowerAtRequiredFrequency(SECOND_GENERATOR_POWER);
		controlUnit_3.setPowerAtRequiredFrequency(THIRD_GENERATOR_POWER);
		generator_1.setControlUnit(controlUnit_1);
		generator_2.setControlUnit(controlUnit_2);
		generator_3.setControlUnit(controlUnit_3);
		generator_1.setId(1);
		generator_2.setId(2);
		generator_3.setId(3);
		generator_1.setNominalPowerInMW(200);
		generator_2.setNominalPowerInMW(200);
		generator_3.setNominalPowerInMW(200);
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
		station.addGenerator(generator_3);
		
		generator_1.turnOnGenerator();
		generator_2.turnOnGenerator();
		
		generator_3.turnOffGenerator();
	}
	
	@Test
	public void PowerOfElectricStationMustBeEqualToSumOfAllTurnedOnGenerators(){
		float sumOfPowerTwoTurnedOnGenerators = FIRST_GENERATOR_POWER + SECOND_GENERATOR_POWER;
		float stationGeneration = station.getCurrentGenerationInMW();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, stationGeneration, 0);
	}
	
	@Test
	public void powerStationParametersContainsCorrectData(){
		
	}
}
