package test.java.com.yvhobby.epsm.model.generation;

import org.junit.*;

import static org.mockito.Mockito.*;

import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class PowerStationTest{
	private PowerStation station;
	private Generator generator_1;
	private Generator generator_2;
	private Generator generator_3;
	private ControlUnit controlUnit_1;
	private ControlUnit controlUnit_2;
	private ControlUnit controlUnit_3;
	
	
	@Before
	public void initialize(){
		station = new PowerStation();
		generator_1 = new Generator();
		generator_2 = new Generator();
		generator_3 = new Generator();
		
		controlUnit_1 = mock(ControlUnit.class);
		controlUnit_2 = mock(ControlUnit.class);
		controlUnit_3 = mock(ControlUnit.class);
		
		when(controlUnit_1.getGeneratorPowerInMW()).thenReturn(100f);
		when(controlUnit_2.getGeneratorPowerInMW()).thenReturn(50f);
		when(controlUnit_3.getGeneratorPowerInMW()).thenReturn(20f);
		
		generator_1.setControlUnit(controlUnit_1);
		generator_2.setControlUnit(controlUnit_2);
		generator_3.setControlUnit(controlUnit_3);
		
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
		station.addGenerator(generator_3);
		
		generator_1.turnOnGenerator();
		generator_2.turnOnGenerator();
		
		generator_3.turnOffGenerator();
	}
	
	@Test
	public void PowerOfElectricStationMustBeEqualToSumOfAllTurnedOnGenerators(){
		float summaryGenerationForStation = station.getCurrentGenerationInMW();
		float sumOfPowerTwoTurnedOnGenerators = 
				generator_1.getGenerationInMW() + generator_2.getGenerationInMW();
		
		Assert.assertEquals(sumOfPowerTwoTurnedOnGenerators, summaryGenerationForStation, 0);
	}
}
