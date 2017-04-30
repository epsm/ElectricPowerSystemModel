package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This implementation does not take in account that generation schedule must be on DATE, not on day. 
public class MainControlPanel{
	private Simulation simulation;
	private PowerStation station;
	private PowerStationGenerationController controller;
	private PowerStationGenerationSchedule currentSchedule;
	private PowerStationGenerationSchedule receivedSchedule;
	private GenerationScheduleValidator validator;
	private PowerStationParameters parameters;
	private Logger logger;

	public MainControlPanel(Simulation simulation, PowerStation station){
		this.simulation = simulation;
		this.station = station;
		controller = new PowerStationGenerationController(simulation, station);
		validator = new GenerationScheduleValidator();
		logger = LoggerFactory.getLogger(MainControlPanel.class);
	}
	
	public void acceptGenerationSchedule(PowerStationGenerationSchedule schedule) {
		performGenerationSchedule(schedule);
	}
	
	private void performGenerationSchedule(PowerStationGenerationSchedule generationSchedule){
		receivedSchedule = generationSchedule;
		getStationParameters();
		
		if(isReceivedScheduleValid()){
			replaceCurrentSchedule();
		}
	}
	
	private void getStationParameters(){
		parameters = (PowerStationParameters) station.getParameters();
	}
	
	private boolean isReceivedScheduleValid(){
		try{
			validator.validate(receivedSchedule, parameters);
			return true;
		}catch (GenerationException exception){
			//TODO send request to dispatcher
			logger.warn("Wrong schedule - {}",exception.getMessage());
			return false;
		}
	}
	
	private void replaceCurrentSchedule(){
		currentSchedule = receivedSchedule;
		logger.info("execute new schedule: {}.", currentSchedule);
	}
	
	private boolean isThereValidSchedule(){
		return currentSchedule != null;
	}
	
	public void adjustGenerators(){
		if(isThereValidSchedule()){
			controller.adjustGenerators(currentSchedule);
		}
	}
}
