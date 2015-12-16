package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithScheduledLoad extends PowerConsumer{
	private ElectricPowerSystemSimulation simulation;
	private RandomLoadCurveFactory factory = new RandomLoadCurveFactory();
	private float[] approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;
	private float degreeOnDependingOfFrequency;
	private LoadCurve loadCurveOnDay;
	private LocalTime previousLoadRequest;
	private LocalTime currentTime;
	private float currentFrequency;
	
	@Override
	public float getCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateLoadCurveOnThisDay();
		}
		
		previousLoadRequest = currentTime;
		
		return getLoadForThisMoment();
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = simulation.getTime();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean isItANewDay(){
		return previousLoadRequest == null || previousLoadRequest.isAfter(currentTime);
	}
	
	private void calculateLoadCurveOnThisDay(){
		loadCurveOnDay = factory.calculateLoadCurve(
				approximateLoadByHoursOnDayInPercent, maxLoadWithoutFluctuationsInMW, randomFluctuationsInPercent);
	}

	private float getLoadForThisMoment(){
		float loadWithoutCountingFrequency = loadCurveOnDay.getPowerOnTimeInMW(currentTime);
		
		return calculateLoadCountingFrequency(loadWithoutCountingFrequency);
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

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
	}

	@Override
	public void setDegreeOfDependingOnFrequency(float degreeOfDependingOfFrequency) {
		this.degreeOnDependingOfFrequency = degreeOfDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
}
