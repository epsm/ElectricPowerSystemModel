package test.java.com.yvhobby.epsm.model.generation;

import org.junit.*;

import static org.mockito.Mockito.*;

import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class GeneratorTest{
	private ControlUnit controlUnit;
	private Generator generator;
	private final float generatorPower = 1000f; 
	
	@Before
	public void initialize(){
		generator = new Generator();
		controlUnit = mock(ControlUnit.class);
		
		when(controlUnit.getGeneratorPowerInMW()).thenReturn(generatorPower);
		generator.setControlUnit(controlUnit);
	}
	
	@Test
	public void GeneratorPowerIsZeroIfItTurnedOff(){
		generator.turnOffGenerator();
		
		Assert.assertEquals(0, generator.getGenerationInMW(), 0);
	}
	
	@Test
	public void isGeneratorPowerEqualsToControlUnitIfItTurnedOn(){
		generator.turnOnGenerator();
		
		Assert.assertEquals(controlUnit.getGeneratorPowerInMW(), generator.getGenerationInMW(), 0);
	}
}
