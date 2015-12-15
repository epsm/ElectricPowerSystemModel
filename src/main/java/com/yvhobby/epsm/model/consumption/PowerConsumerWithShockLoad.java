package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithShockLoad extends PowerConsumer{
	private ElectricPowerSystemSimulation simulation;
	private LocalTime currentTime;
	private int loadDurationInSeconds;
	private int pauseBetweenWorkInSeconds;
	private float maxLoad;
	private float currentLoad;
	private float currentFrequency;
	private float degreeOnDependingOnFrequency;
	private LocalTime timeToTurnOn = LocalTime.MIDNIGHT;//for first time else NPE
	private LocalTime timeToTurnOff;
	private boolean isTurnedOn;
	
	@Override
	public float getCurrentConsumptionInMW(){
		currentTime = simulation.getTime();
		currentFrequency = simulation.getFrequencyInPowerSystem();

		if(isTurnedOn){
			if(IsItTimeToTurnOff()){
				turnOffAndSetTimeToTurnOn();
			}
		}else{
			if(IsItTimeToTurnOn()){
				turnOnAndSetTimeToTurnOff();
			}else{
				return 0;
			}
		}
		
		return calculateConsumptionCountingCurrentFrequency(currentLoad);
	}

	private boolean IsItTimeToTurnOn(){
		return timeToTurnOn.isBefore(currentTime);
	}
	
	private void turnOnAndSetTimeToTurnOff(){
		turnOnWithRandomLoadValue();
		setTimeToTurnOff();
	}
	
	private void turnOnWithRandomLoadValue(){
		float halfOfMaxLoad = maxLoad / 2;
		currentLoad = halfOfMaxLoad + halfOfMaxLoad * (float)Math.random();
		isTurnedOn = true;
	}
	
	private void setTimeToTurnOff(){
		float halfOfTurnedOnDuration = loadDurationInSeconds / 2; 
		timeToTurnOff = currentTime.plusSeconds(
				(long)(halfOfTurnedOnDuration + halfOfTurnedOnDuration * Math.random()));
	}
	
	private boolean IsItTimeToTurnOff(){
		return timeToTurnOff.isBefore(currentTime);
	}
	
	private void turnOffAndSetTimeToTurnOn(){
		turnOff();
		setTimeToTurnOn();
	}
	
	private void turnOff(){
		currentLoad = 0;
		isTurnedOn = false;
	}
	
	private void setTimeToTurnOn(){
		float halfOfTurnedOffDuration = pauseBetweenWorkInSeconds / 2; 
		timeToTurnOn = currentTime.plusSeconds(
				(long)(halfOfTurnedOffDuration + halfOfTurnedOffDuration * Math.random()));
	}
	
	private float calculateConsumptionCountingCurrentFrequency(float consumption){
		return (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOnFrequency) * consumption;
	}
	
	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation powerSystemSimulation) {
		simulation = powerSystemSimulation;
	}

	public void setLoadDurationInSeconds(int loadDurationInSeconds) {
		this.loadDurationInSeconds = loadDurationInSeconds;
	}

	public void setPauseBetweenWorkInSeconds(int durationBetweenWorkInSeconds) {
		this.pauseBetweenWorkInSeconds = durationBetweenWorkInSeconds;
	}
		
	public void setMaxLoad(float maxLoad) {
		this.maxLoad = maxLoad;
	}

	@Override
	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency) {
		this.degreeOnDependingOnFrequency = degreeOnDependingOfFrequency;
	}
}
