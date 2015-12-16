package test.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import test.java.com.yvhobby.epsm.model.constantsForTests.TestsConstants;

public class LoadCurveTest{
	private float[] loadByHours = TestsConstants.LOAD_BY_HOURS;
	private LoadCurve curve = new LoadCurve(loadByHours);
	
	@Test
	public void loadByHoursEqualsToOriginal(){
		for(int i = 0; i < 24; i++){
			float orginalLoad = loadByHours[i];
			float loadReturnedByLoadCurve = curve.getPowerOnTimeInMW(LocalTime.of(i, 0));
			
			Assert.assertEquals(orginalLoad, loadReturnedByLoadCurve, 0);
		}
	}
	
	@Test
	public void scheduleInterpolized(){
		LocalTime pointer = LocalTime.MIDNIGHT;
		float currentLoad = 0;
		float previousLoad = 0;
		float actualDdelta = 0;
		float maxDelta = 0;
		
		maxDelta = calculateMaxDelta();
		
		do{
			currentLoad = curve.getPowerOnTimeInMW(pointer);
			previousLoad = curve.getPowerOnTimeInMW(pointer.minusSeconds(1));
			actualDdelta = Math.abs(currentLoad - previousLoad);
			
			Assert.assertTrue(actualDdelta <= maxDelta);

			pointer = pointer.plusSeconds(1);
		}while(pointer.isAfter(LocalTime.MIDNIGHT));
	}
	
	private float calculateMaxDelta(){
		float maxLoadValue = Float.MIN_VALUE;
		float minLoadValue = Float.MAX_VALUE;
		float maxlDelta = 0;
		final int secondsInHour = 60 * 60;
		
		for(int i = 0; i < 24; i++){
			float currentLoad = curve.getPowerOnTimeInMW(LocalTime.of(i, 0));
			
			if(maxLoadValue < currentLoad){
				maxLoadValue = currentLoad;
			}
			
			if(minLoadValue > currentLoad){
				minLoadValue = currentLoad;
			}
		}
		
		Assert.assertNotEquals(maxLoadValue, Float.MIN_VALUE, 0);
		Assert.assertNotEquals(minLoadValue, Float.MAX_VALUE, 0);
		
		maxlDelta = (maxLoadValue - minLoadValue) / (secondsInHour);
		
		return maxlDelta;
	}
}
