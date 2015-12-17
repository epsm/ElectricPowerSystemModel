package test.java.com.yvhobby.epsm.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulationUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class AstaticRegulationUnitTest {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulationUnit regulationUnit;
	private ControlUnit controlUnit;
	private Generator generator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator();
		regulationUnit = new AstaticRegulationUnit(simulation, generator);
		controlUnit = new ControlUnit(simulation, generator);
		
		generator.setAstaticRegulationUnit(regulationUnit);
		generator.setControlUnit(controlUnit);
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
		generator.setMinimalTechnologyPower(100);
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				controlUnit.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
}
