package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public class DailyConsumptionPattern {
	
	private float[] loadingInPercentsForHours = new float[]{
		64.88f,  59.54f,  55.72f,  51.9f,
		48.47f,  48.85f,  48.09f,  57.25f,
		76.35f,  91.60f,  100,0f,  99.23f,
		91.60f,  91.60f,  91.22f,  90.83f,
		90.83f,  90.83f,  90.83f,  90.83f, 
		90.83f,  90.83f,  90.83f,  83.96f 
	};

	public float getPowerInPercentForCurrentHour(LocalTime currentTime){
		int currentHour = currentTime.getHour();
		
		return loadingInPercentsForHours[currentHour];
	}
}
