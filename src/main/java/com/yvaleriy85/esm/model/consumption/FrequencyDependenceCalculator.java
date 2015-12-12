package main.java.com.yvaleriy85.esm.model.consumption;

import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class FrequencyDependenceCalculator {
	
	
	public static float calculateConsumption(float consumptionWithoutCountingFrequency,
			float frequency, float degreeOfDependingOfFrequency){
		
		return (float)Math.pow((frequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOfDependingOfFrequency) * consumptionWithoutCountingFrequency;
	}
}
