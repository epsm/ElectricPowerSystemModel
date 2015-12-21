package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithShockLoad extends PowerConsumer{
	private ElectricPowerSystemSimulation simulation;
	private LocalTime currentTime;
	private int maxWorkDurationInSeconds;
	private int maxPauseBetweenWorkInSeconds;
	private float maxLoad;
	private float currentLoad;
	private float currentFrequency;
	private float degreeOnDependingOnFrequency;
	private LocalTime timeToTurnOn = LocalTime.MIDNIGHT;//for first time else NPE
	private LocalTime timeToTurnOff;
	private boolean isTurnedOn;
	private Random random = new Random();
	private Logger logger = (Logger) LoggerFactory.getLogger(PowerConsumerWithShockLoad.class);
	
	@Override
	public float getCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();

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
		
		return calculateLoadCountingFrequency(currentLoad);
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = simulation.getTime();
		currentFrequency = simulation.getFrequencyInPowerSystem();
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
		currentLoad = halfOfMaxLoad + halfOfMaxLoad * random.nextFloat();
		isTurnedOn = true;
	}
	
	private void setTimeToTurnOff(){
		float halfOfTurnedOnDuration = maxWorkDurationInSeconds / 2; 
		timeToTurnOff = currentTime.plusSeconds(
				(long)(halfOfTurnedOnDuration + halfOfTurnedOnDuration * random.nextFloat()));
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
		float halfOfTurnedOffDuration = maxPauseBetweenWorkInSeconds / 2; 
		timeToTurnOn = currentTime.plusSeconds(
				(long)(halfOfTurnedOffDuration + halfOfTurnedOffDuration * random.nextFloat()));
	}
	
	private float calculateLoadCountingFrequency(float load){
		return (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOnFrequency) * load;
	}
	
	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
		
		logger.info("Power consumer with shock load was added to simulation.");
	}

	public void setMaxWorkDurationInSeconds(int WorkDurationInSeconds) {
		this.maxWorkDurationInSeconds = WorkDurationInSeconds;
	}

	public void setMaxPauseBetweenWorkInSeconds(int durationBetweenWorkInSeconds) {
		this.maxPauseBetweenWorkInSeconds = durationBetweenWorkInSeconds;
	}
		
	public void setMaxLoad(float maxLoad) {
		this.maxLoad = maxLoad;
	}

	@Override
	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency) {
		this.degreeOnDependingOnFrequency = degreeOnDependingOfFrequency;
	}
}
