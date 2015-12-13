package test.java.com.yvaleriy85.esm.model.consumption;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.consumption.FrequencyDependenceCalculator;
import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class FrequencyDependenceCalculatorTest {

	@Test
	public void testOnRightCalculationWithSecondDegree(){
		float consumption = FrequencyDependenceCalculator.calculateConsumption(100, 100, 2);
		float expectedValue = (float)Math.pow((100 / GlobalConstatnts.STANDART_FREQUENCY), 2) * 100;
		
		Assert.assertEquals(expectedValue, consumption, 0);
	}
}
