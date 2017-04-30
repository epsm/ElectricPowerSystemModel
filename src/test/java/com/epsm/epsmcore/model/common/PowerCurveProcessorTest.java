package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.simulation.Constants;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalTime;
import java.util.List;

public class PowerCurveProcessorTest {

	private PowerCurveProcessor curveProcessor = new PowerCurveProcessor();
	private List<Float> loadByHours = TestsConstants.LOAD_BY_HOURS;
	private PowerCurve curve = new PowerCurve(loadByHours);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void loadByHoursEqualsToOriginal(){
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			float orginalLoad = loadByHours.get(i);
			float loadReturnedByProcessory = curveProcessor.getPowerOnTimeInMW(curve, LocalTime.of(i, 0));
			
			Assert.assertEquals(orginalLoad, loadReturnedByProcessory, 0);
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
			currentLoad = curveProcessor.getPowerOnTimeInMW(curve, pointer);
			previousLoad = curveProcessor.getPowerOnTimeInMW(curve, pointer.minusSeconds(1));
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
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
			float currentLoad = curveProcessor.getPowerOnTimeInMW(curve, LocalTime.of(i, 0));
			
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
