package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class ScheduledLoadConsumer extends Consumer{
	private LoadCurveFactory factory = new LoadCurveFactory();
	private float[] approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;
	private LoadCurve loadCurveOnDay;
	private LocalTime previousLoadRequestTime;
	private ConsumerState state;
	private LocalTime currentTime;
	private float currentLoad;
	private float currentFrequency;
	private Logger logger;
	
	public ScheduledLoadConsumer(int consumerNumber, ElectricPowerSystemSimulation simulation) {
		super(consumerNumber, simulation);
		logger = LoggerFactory.getLogger(ScheduledLoadConsumer.class);
		logger.info("Scheduled load consumer №" + consumerNumber + " created.");
	}
	
	@Override
	public float calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateLoadCurveOnThisDay();
		}
		
		saveRequestTime();
		calculateLoadForThisMoment();
		currentLoad = calculateLoadCountingFrequency(currentLoad, currentFrequency);
		state = prepareState(currentTime, currentLoad);
		
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
		loadCurveOnDay = factory.getRandomCurve(
				approximateLoadByHoursOnDayInPercent, maxLoadWithoutFluctuationsInMW, randomFluctuationsInPercent);
	}

	private void saveRequestTime(){
		previousLoadRequestTime = currentTime;
	}
	
	private void calculateLoadForThisMoment(){
		currentLoad = loadCurveOnDay.getPowerOnTimeInMW(currentTime);
	}
	
	@Override
	public PowerObjectState getState() {
		return state;
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
