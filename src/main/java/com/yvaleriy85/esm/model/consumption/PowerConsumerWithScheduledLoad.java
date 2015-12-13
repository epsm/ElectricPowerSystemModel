package main.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithScheduledLoad extends PowerConsumer{
	private ElectricPowerSystemSimulation powerSystemSimulation;
	private ConsumptionScheduleCalculator calculator = new ConsumptionScheduleCalculator();
	private DailyConsumptionPattern dailyPattern;
	private float maxConsumptionWithoutRandomInMW;
	private float randomComponentInPercent;
	private float degreeOnDependingOfFrequency;
	private ConsumptionSchedule dayConsumptionSchedule;
	private LocalTime lastRequest;
	private LocalTime currentTime;
	private float currentFrequency;
	
	@Override
	public float getCurrentConsumptionInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateConsumptionScheduleOnThisDay();
		}
		
		lastRequest = currentTime;
		
		return getConsumptionForThisMoment();
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = powerSystemSimulation.getTime();
		currentFrequency = powerSystemSimulation.getFrequencyInPowerSystem();
	}
	
	private boolean isItANewDay(){
		return lastRequest == null || lastRequest.isBefore(currentTime);
	}
	
	private void calculateConsumptionScheduleOnThisDay(){
		dayConsumptionSchedule = calculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponentInPercent);
	}

	private float getConsumptionForThisMoment(){
		float consumptionWithoutCountingFrequency =
				dayConsumptionSchedule.getConsumptionOnTime(currentTime);
		
		return calculateConsumptionCountingCurrentFrequency(consumptionWithoutCountingFrequency);
	}
	
	private float calculateConsumptionCountingCurrentFrequency(float consumption){
		return (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * consumption;
		
		//return consumption;
	}
	
	public void setRandomComponentInPercent(float randomComponentInPercent) {
		this.randomComponentInPercent = randomComponentInPercent;
	}

	public float getRandomComponentInPercent() {
		return randomComponentInPercent;
	}

	public void setDailyPattern(DailyConsumptionPattern dailyPattern) {
		this.dailyPattern = dailyPattern;
	}

	public void setMaxConsumptionWithoutRandomInMW(float maxConsumptionWithoutRandomInMW) {
		this.maxConsumptionWithoutRandomInMW = maxConsumptionWithoutRandomInMW;
	}

	public float getMaxConsumptionWithoutRandomInMW() {
		return maxConsumptionWithoutRandomInMW;
	}

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOfDependingOfFrequency) {
		this.degreeOnDependingOfFrequency = degreeOfDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
}
