package test.java.com.yvhobby.epsm.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulatioUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class AstaticRegulatioUnitTest {
	private AstaticRegulatioUnit regulationUnit;
	private ControlUnit controlUnit;
	private Generator generator;
	
	@Before
	public void initialize(){
		regulationUnit = new AstaticRegulatioUnit();
		controlUnit = new ControlUnit();
		generator = new Generator();
		
		regulationUnit.setControlUnit(controlUnit);
		regulationUnit.setGenerator(generator);
		controlUnit.setPowerAtRequiredFrequency(100);
		generator.setNominalPowerInMW(200);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY - 1);
		
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() > 100);
	}
	
	@Test
	public void decreasePowerIfFrequencyIsHight(){
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY + 1);
		
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() < 100);
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndLessThanZero(){
		float regulationError = 0.02f;
		
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY - regulationError);
		
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() == 100);
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndMoreThanZero(){
		float regulationError = 0.02f;
		
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY + regulationError);
			
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() == 100);
	}
	
	@Test
	public void doNothingIfFrequencyIsLowAndGeneratorPowerIsMaximal(){
		generator.setNominalPowerInMW(100);
			
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY - 1);
		
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() == 100);
	}
	
	@Test
	public void doNothingIfFrequencyIsLowAndGeneratorPowerIsMinimal(){
		generator.setMinimalTechnologyPower(100);
	
		regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(
				GlobalConstatnts.STANDART_FREQUENCY + 1);
		
		Assert.assertTrue(controlUnit.getPowerAtRequiredFrequency() == 100);
	}
}
