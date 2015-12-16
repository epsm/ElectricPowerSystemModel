package test.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.consumption.RandomLoadCurveFactory;
import test.java.com.yvhobby.epsm.model.constantsForTests.TestsConstants;

public class RandomLoadCurveFactoryTest {
	private float[] originalLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
	private float maxLoadWithoutRandomInMW = 100;
	private float randomFluctuaton = 10;
	private LoadCurve curve;
	private RandomLoadCurveFactory factory = new RandomLoadCurveFactory();

	@Test
	public void conformityOriginalLoadAndLoadAccordingToCurveCountingRandomFluctuations(){
		curve = factory.calculateLoadCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < 24; i++){
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
		LoadCurve curve_1 = factory.calculateLoadCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		LoadCurve curve_2 = factory.calculateLoadCurve(
				originalLoadByHoursInPercent, maxLoadWithoutRandomInMW, randomFluctuaton);
		
		for(int i = 0; i < 24; i++){
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
