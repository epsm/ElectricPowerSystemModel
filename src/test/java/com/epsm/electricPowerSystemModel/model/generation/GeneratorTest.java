package test.java.com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;

public class GeneratorTest{
	private Generator generator;
	private StaticRegulator staticRegulator;
	private AstaticRegulator astaticRegulator;
	private final float GENERATOR_GENERATION = 1000f; 
	
	@Before
	public void initialize(){
		generator = new Generator(1);
		staticRegulator = mock(StaticRegulator.class);
		
		when(staticRegulator.getGeneratorPowerInMW()).thenReturn(GENERATOR_GENERATION);
		generator.setStaticRegulator(staticRegulator);
		generator.turnOnGenerator();
	}
	
	@Test
	public void GeneratorPowerIsZeroIfItTurnedOff(){
		generator.turnOffGenerator();
		
		Assert.assertEquals(0, generator.calculateGeneration(), 0);
	}
	
	@Test
	public void isGeneratorPowerEqualsToControlUnitIfItTurnedOn(){
		generator.turnOnGenerator();
		
		Assert.assertEquals(staticRegulator.getGeneratorPowerInMW(), generator.calculateGeneration(), 0);
	}
	
	@Test
	public void DoesAstacicRegulatorUsesIfItTurnedOn(){
		prepareMockAstaticRegulator();
		generator.turnOnAstaticRegulation();
		generator.calculateGeneration();
		
		verify(astaticRegulator, times(1)).verifyAndAdjustPowerAtRequiredFrequency();
	}
	
	private void prepareMockAstaticRegulator(){
		astaticRegulator = mock(AstaticRegulator.class);
		generator.setAstaticRegulator(astaticRegulator);
	}
}
