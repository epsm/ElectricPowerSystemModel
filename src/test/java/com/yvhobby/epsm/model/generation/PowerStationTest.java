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
	
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		station = new PowerStation();
		generator_1 = new Generator(1);
		generator_2 = new Generator(2);
		generator_3 = new Generator(3);
		controlUnit_1 = new ControlUnit();
		controlUnit_2 = new ControlUnit();
		controlUnit_3 = new ControlUnit();
		
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstatnts.STANDART_FREQUENCY);
		
		controlUnit_1.setElectricPowerSystemSimulation(simulation);
		controlUnit_2.setElectricPowerSystemSimulation(simulation);
		controlUnit_3.setElectricPowerSystemSimulation(simulation);
		controlUnit_1.setPowerAtRequiredFrequency(100f);
		controlUnit_2.setPowerAtRequiredFrequency(50f);
		controlUnit_3.setPowerAtRequiredFrequency(20f);
		generator_1.setControlUnit(controlUnit_1);
		generator_2.setControlUnit(controlUnit_2);
		generator_3.setControlUnit(controlUnit_3);
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
		station.addGenerator(generator_3);
		
		controlUnit_1.turnOnGenerator();
		controlUnit_2.turnOnGenerator();
		
		controlUnit_3.turnOffGenerator();
	}
	
	@Test
	public void PowerOfElectricStationMustBeEqualToSumOfAllTurnedOnGenerators(){
		float summaryGenerationForStation = station.getCurrentGenerationInMW();
		float sumOfPowerTwoTurnedOnGenerators = 
				generator_1.getGenerationInMW() + generator_2.getGenerationInMW();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, summaryGenerationForStation, 0);
	}
}
