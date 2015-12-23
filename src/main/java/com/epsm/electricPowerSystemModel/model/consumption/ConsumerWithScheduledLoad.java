package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class ConsumerWithScheduledLoad extends Consumer{
	private RandomLoadCurveFactory factory = new RandomLoadCurveFactory();
	private float[] approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;
	private LoadCurve loadCurveOnDay;
	private LocalTime previousLoadRequestTime;
	private float currentFrequency;
	private Logger logger = (Logger) LoggerFactory.getLogger(ConsumerWithScheduledLoad.class);
	
	public ConsumerWithScheduledLoad(int consumerNumber) {
		super(consumerNumber);
		logger.info("Consumer ¹" + consumerNumber + " with scheduled load created");
	}
	
	@Override
	public float calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateLoadCurveOnThisDay();
		}
		
		saveRequestTime();
		calculateLoadForThisMoment();
		
		return currentLoad;
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = simulation.getTime();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean isItANewDay(){
		return previousLoadRequestTime == null || previousLoadRequestTime.isAfter(currentTime);
	}
	
	private void calculateLoadCurveOnThisDay(){
		loadCurveOnDay = factory.calculateLoadCurve(
				approximateLoadByHoursOnDayInPercent, maxLoadWithoutFluctuationsInMW, randomFluctuationsInPercent);
	}

	private void saveRequestTime(){
		previousLoadRequestTime = currentTime;
	}
	
	private void calculateLoadForThisMoment(){
		float loadWithoutCountingFrequency = loadCurveOnDay.getPowerOnTimeInMW(currentTime);
		currentLoad =  calculateLoadCountingFrequency(loadWithoutCountingFrequency);
	}
	
	private float calculateLoadCountingFrequency(float loadWithoutCountingFrequency){
		return (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * loadWithoutCountingFrequency;
	}
	
	public void setRandomFluctuationsInPercent(float randomFluctuationsInPercent) {
		this.randomFluctuationsInPercent = randomFluctuationsInPercent;
	}

	public float getRandomFluctuationsInPercent() {
		return randomFluctuationsInPercent;
	}

	public void setApproximateLoadByHoursOnDayInPercent(float[] approximateLoadByHoursOnDayInPercent) {
		this.approximateLoadByHoursOnDayInPercent = approximateLoadByHoursOnDayInPercent;
	}

	public void setMaxLoadWithoutRandomInMW(float maxLoadWithoutRandomInMW) {
		this.maxLoadWithoutFluctuationsInMW = maxLoadWithoutRandomInMW;
	}

	public float getMaxLoadWithoutRandomInMW() {
		return maxLoadWithoutFluctuationsInMW;
	}
}
