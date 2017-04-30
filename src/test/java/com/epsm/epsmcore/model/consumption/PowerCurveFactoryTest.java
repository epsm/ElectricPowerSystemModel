package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.simulation.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PowerCurveFactoryTest {

	private List<Float> originalLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
	private float maxLoadWithoutRandomInMW = 100;
	private float randomFluctuaton = 10;
	private PowerCurve curve;
	private LoadCurveFactory curveFactory = new LoadCurveFactory();

	@Test
	public void conformityOriginalLoadAndLoadAccordingToCurveCountingRandomFluctuations(){
		curve = curveFactory.getRandomCurve(originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			float originalLoadInMW = calculateOriginalLoadInMW(i);
			float LoadAccordingToCurve = curve.getPowerByHoursInMW().get(i);
			float permissibleDelta = calculatePermissibleDeltaInMW(originalLoadInMW);
			
			Assert.assertEquals(originalLoadInMW, LoadAccordingToCurve, permissibleDelta);
		}
	}
	
	private float calculateOriginalLoadInMW(int hour){
		float originalBasePartInPercent = originalLoadByHoursInPercent.get(hour);
		
		return originalBasePartInPercent * maxLoadWithoutRandomInMW / 100;
	}
	
	private float calculatePermissibleDeltaInMW(float load){
		return load * randomFluctuaton / 100;
	}
	
	@Test
	public void constructedLoadCurvesAreRandom(){
		boolean atLeastOneLoadValueIsUnique = false;
		PowerCurve curve_1 = curveFactory.getRandomCurve(originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		PowerCurve curve_2 = curveFactory.getRandomCurve(originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			double valueFromCurve_1 = curve_1.getPowerByHoursInMW().get(i);
			double valueFromCurve_2 = curve_2.getPowerByHoursInMW().get(i);
			
			if(valueFromCurve_1 != valueFromCurve_2){
				atLeastOneLoadValueIsUnique = true;
				break;
			}
		}

		Assert.assertTrue(atLeastOneLoadValueIsUnique);
	}
}
