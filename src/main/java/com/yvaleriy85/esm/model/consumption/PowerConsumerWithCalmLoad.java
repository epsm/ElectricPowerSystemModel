package main.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithCalmLoad extends PowerConsumer{
	private ElectricPowerSystemSimulationImpl powerSystemSimulation;
	private DailyConsumptionPattern dailyPattern;
	private float maxConsumptionWithoutRandomInMW;
	private float randomComponentInPercent;
	private float degreeOfDependingOfFrequency;
	private ConsumptionSchedule dayConsumptionSchedule;
	private LocalTime lastRequest = LocalTime.of(23,59,59,999_999_999);
	private LocalTime currentTime;
	private float currentFrequency;
	private boolean objectParametersWereChanged = true;
	
	@Override
	public float getCurrentConsumptionInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(objectParametersWereChanged){
			recalculateState();
		}
		
		if(isItANewDay()){
			calculateConsumptionScheduleOnThisDay();
		}
		
		return getConsumptionForThisMoment();
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = powerSystemSimulation.getTime();
		currentFrequency = powerSystemSimulation.getFrequencyInPowerSystem();
	}
	
	private void recalculateState(){
		calculateConsumptionScheduleOnThisDay();
		objectParametersWereChanged = false;
	}
	
	private boolean isItANewDay(){
		return lastRequest.isBefore(currentTime);
	}
	
	private void calculateConsumptionScheduleOnThisDay(){
		dayConsumptionSchedule = ConsumptionScheduleCalculator.
				calculateConsumptionScheduleInMW(dailyPattern,
				maxConsumptionWithoutRandomInMW, randomComponentInPercent);
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
		objectParametersWereChanged = true;
	}

	public void setDailyPattern(DailyConsumptionPattern dailyPattern) {
		this.dailyPattern = dailyPattern;
		objectParametersWereChanged = true;
	}

	public void setMaxConsumptionWithoutRandomInMW(float maxConsumptionWithoutRandomInMW) {
		this.maxConsumptionWithoutRandomInMW = maxConsumptionWithoutRandomInMW;
		objectParametersWereChanged = true;
	}

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulationImpl powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;
		objectParametersWereChanged = true;
	}

	public void setDegreeOfDependingOfFrequency(float degreeOfDependingOfFrequency) {
		this.degreeOfDependingOfFrequency = degreeOfDependingOfFrequency;
	}
}
