package com.epsm.epsmCore.model.consumption;

import java.time.LocalTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerCurve;
import org.junit.Assert;
import org.junit.Test;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;

public class PowerCurveFactoryTest {
	private float[] originalLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
	private float maxLoadWithoutRandomInMW = 100;
	private float randomFluctuaton = 10;
	private PowerCurve curve;
	private LoadCurveFactory builder = new LoadCurveFactory();

	@Test
	public void conformityOriginalLoadAndLoadAccordingToCurveCountingRandomFluctuations(){
		curve = builder.getRandomCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			float originalLoadInMW = calculateOriginalLoadInMW(i);
			float LoadAccordingToCurve = curve.getPowerOnTimeInMW(LocalTime.of(i, 0));
			float permissibleDelta = calculatePermissibleDeltaInMW(originalLoadInMW);
			
			Assert.assertEquals(originalLoadInMW, LoadAccordingToCurve, permissibleDelta);
		}
	}
	
	private float calculateOriginalLoadInMW(int hour){
		float originalBasePartInPercent = originalLoadByHoursInPercent[hour];
		
		return originalBasePartInPercent * maxLoadWithoutRandomInMW / 100;
	}
	
	private float calculatePermissibleDeltaInMW(float load){
		return load * randomFluctuaton / 100;
	}
	
	@Test
	public void constructedLoadCurvesAreRandom(){
		boolean atLeastOneLoadValueIsUnique = false;
		PowerCurve curve_1 = builder.getRandomCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		PowerCurve curve_2 = builder.getRandomCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			double valueFromCurve_1 = curve_1.getPowerOnTimeInMW(LocalTime.of(i, 0));
			double valueFromCurve_2 = curve_2.getPowerOnTimeInMW(LocalTime.of(i, 0));
			
			if(valueFromCurve_1 != valueFromCurve_2){
				atLeastOneLoadValueIsUnique = true;
				break;
			}
		}

		Assert.assertTrue(atLeastOneLoadValueIsUnique);
	}
}
