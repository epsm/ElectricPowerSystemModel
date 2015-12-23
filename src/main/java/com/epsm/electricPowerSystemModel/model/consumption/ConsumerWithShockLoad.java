package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class ConsumerWithShockLoad extends PowerConsumer{
	private int maxWorkDurationInSeconds;
	private int maxPauseBetweenWorkInSeconds;
	private float maxLoad;
	private float currentLoadWithoutFrequencyCounting;
	private float currentFrequency;
	private LocalTime timeToTurnOn = LocalTime.MIDNIGHT;//for first time else NPE
	private LocalTime timeToTurnOff;
	private boolean isTurnedOn;
	private Random random = new Random();
	private Logger logger = (Logger) LoggerFactory.getLogger(ConsumerWithShockLoad.class);
	
	public ConsumerWithShockLoad(int consumerNumber) {
		super(consumerNumber);
		logger.info("Consumer ¹" + consumerNumber + " with shock load created");
	}
	
	@Override
	public float calculateCurrentLoadInMW(){
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
		
		calculateLoadCountingFrequency(currentLoadWithoutFrequencyCounting);
		
		return currentLoad;
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
		currentLoadWithoutFrequencyCounting = halfOfMaxLoad + halfOfMaxLoad * random.nextFloat();
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
	
	private void calculateLoadCountingFrequency(float load){
		currentLoad = (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
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
}
