package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.generation.GenerationException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalTime;

public class PowerCurveTest {
	private float[] loadByHours = TestsConstants.LOAD_BY_HOURS;
	private PowerCurve curve = new PowerCurve(loadByHours);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void loadByHoursEqualsToOriginal(){
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
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
		
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY; i++){
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
	
	@Test
	public void constructorThrowsExceptionIfIncomingArrayIsNull(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("PowerCurve constructor: loadByHoursInMW must not be null.");
	    
		new PowerCurve(null);
	}
	
	@Test
	public void constructorThrowsExceptionIfIncomingArrayLenghtIsNot24(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Incoming array length must be 24.");
	    
	    new PowerCurve(new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ,11});
	}
	
	@Test
	public void toStringWorksCorrect(){
		String expected = "<PowerCurve: load in MW on day by hours, starts on 00.00: "
				+ "[64.88, 59.54, 55.72, 51.9, 48.47, 48.85,"
				+ " 48.09, 57.25, 76.35, 91.6, 100.0, 99.23,"
				+ " 91.6, 91.6, 91.22, 90.83, 90.83, 90.83,"
				+ " 90.83, 90.83, 90.83, 90.83, 90.83, 83.96]>";
		String toStringResult = curve.toString();
		
		Assert.assertEquals(expected, toStringResult);
	}
}
