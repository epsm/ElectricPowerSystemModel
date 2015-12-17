package test.java.com.yvhobby.epsm.model.generation;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.generation.AstaticRegulationUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class GeneratorTest{
	private Generator generator;
	private ControlUnit controlUnit;
	private AstaticRegulationUnit regulationUnit;
	private final float GENERATION = 1000f; 
	
	@Before
	public void initialize(){
		generator = new Generator();
		controlUnit = mock(ControlUnit.class);
		
		when(controlUnit.getGeneratorPowerInMW()).thenReturn(GENERATION);
		generator.setControlUnit(controlUnit);
		generator.turnOnGenerator();
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
	
	@Test
	public void DoesAstacicRegulationUnitUsesIfItTurnedOn(){
		prepareMockAstaticRegulationUnit();
		generator.TurnOnAstaticRegulation();
		generator.getGenerationInMW();
		
		verify(regulationUnit, times(1)).verifyAndAdjustPowerAtRequiredFrequency();
	}
	
	private void prepareMockAstaticRegulationUnit(){
		regulationUnit = mock(AstaticRegulationUnit.class);
		generator.setAstaticRegulationUnit(regulationUnit);
	}
}
