package main.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithScheduledLoad extends PowerConsumer{
	private ElectricPowerSystemSimulationImpl powerSystemSimulation;
	private ConsumptionScheduleCalculator calculator = new ConsumptionScheduleCalculator();
	private DailyConsumptionPattern dailyPattern;
	private float maxConsumptionWithoutRandomInMW;
	private float randomComponentInPercent;
	private float degreeOfDependingOfFrequency;
	private ConsumptionSchedule dayConsumptionSchedule;
	private LocalTime lastRequest = getLastMomentInDay();
	private LocalTime currentTime;
	private float currentFrequency;
	private boolean consumerParametersWereChanged = true;
	
	private LocalTime getLastMomentInDay(){
		return LocalTime.of(23, 59, 59, 999_999_999);
	}
	
	@Override
	public float getCurrentConsumptionInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay() || consumerParametersWereChanged){
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
		return lastRequest.isBefore(currentTime);
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
				degreeOfDependingOfFrequency) * consumption;
	}
	
	public void setRandomComponentInPercent(float randomComponentInPercent) {
		this.randomComponentInPercent = randomComponentInPercent;
		consumerParametersWereChanged = true;
	}

	public void setDailyPattern(DailyConsumptionPattern dailyPattern) {
		this.dailyPattern = dailyPattern;
		consumerParametersWereChanged = true;
	}

	public void setMaxConsumptionWithoutRandomInMW(float maxConsumptionWithoutRandomInMW) {
		this.maxConsumptionWithoutRandomInMW = maxConsumptionWithoutRandomInMW;
		consumerParametersWereChanged = true;
	}

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulationImpl powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;
		consumerParametersWereChanged = true;
	}

	public void setDegreeOfDependingOfFrequency(float degreeOfDependingOfFrequency) {
		this.degreeOfDependingOfFrequency = degreeOfDependingOfFrequency;
	}
}
