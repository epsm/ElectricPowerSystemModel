package test.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;


import main.java.com.yvaleriy85.esm.model.DailyPattern;

public class DailyPatternTest {
	
	DailyPattern dailyPatern = new DailyPattern();
	
	private float[] loadingInPercentsForHours = new float[]{
		64.88f,  59.54f,  55.72f,  51.9f,
		48.47f,  48.85f,  48.09f,  57.25f,
		76.35f,  91.60f,  100,0f,  99.23f,
		91.60f,  91.60f,  91.22f,  90.83f,
		90.83f,  90.83f,  90.83f,  90.83f, 
		90.83f,  90.83f,  90.83f,  83.96f 
	};

	@Test
	public void testReturnedValues(){
		for(int i = 0; i < 24; i++){
			LocalTime currentHour = prepareLocalTime(i);
			float valueOnCurrentHour =
					dailyPatern.getPowerInPercentForCurrentHour(currentHour);

			Assert.assertEquals(valueOnCurrentHour, loadingInPercentsForHours[i], 0);
			
		}
	}
	
	private LocalTime prepareLocalTime(int hour){
		return LocalTime.of(hour, 0);
	}
	
}
