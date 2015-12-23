package test.java.com.epsm.electricPowerSystemModel.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;
import main.java.com.epsm.electricPowerSystemModel.model.generation.AstaticRegulatort;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;

public class AstaticRegulationUnitTest {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulatort regulationUnit;
	private StaticRegulator controlUnit;
	private Generator generator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator(1);
		regulationUnit = new AstaticRegulatort(simulation, generator);
		controlUnit = new StaticRegulator(simulation, generator);
		
		generator.setAstaticRegulator(regulationUnit);
		generator.setStaticRegulator(controlUnit);
		generator.setNominalPowerInMW(200);
		controlUnit.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(GlobalConstatnts.STANDART_FREQUENCY - 0.1));
	}
	
	@Test
	public void decreasePowerIfFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(GlobalConstatnts.STANDART_FREQUENCY + 0.1));
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndLessThanZero(){
		prepareMockSimulationWithLittleLowerButPermissibleFrequency();
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);

	}
	
	private void prepareMockSimulationWithLittleLowerButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(GlobalConstatnts.STANDART_FREQUENCY - 0.02));
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndMoreThanZero(){
		prepareMockSimulationWithLittleHigherButPermissibleFrequency();
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
			
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLittleHigherButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(GlobalConstatnts.STANDART_FREQUENCY + 0.02));
	}
	
	@Test
	public void doNothingIfFrequencyIsLowAndGeneratorPowerIsMaximal(){
		prepareMockSimulationWithLowFrequency();
		generator.setNominalPowerInMW(100);
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	@Test
	public void doNothingIfFrequencyIsHighAndGeneratorPowerIsMinimal(){
		prepareMockSimulationWithHighFrequency();
		generator.setMinimalPowerInMW(100);
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
}
