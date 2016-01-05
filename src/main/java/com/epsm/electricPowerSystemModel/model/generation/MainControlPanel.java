package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class MainControlPanel{
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station;
	private GeneratorsController controller;
	private PowerStationGenerationSchedule currentSchedule;
	private PowerStationGenerationSchedule receivedSchedule;
	private GenerationScheduleValidator validator;
	private PowerStationParameters parameters;
	private Logger logger;

	public MainControlPanel(ElectricPowerSystemSimulation simulation, PowerStation station){
		this.simulation = simulation;
		this.station = station;
		controller = new GeneratorsController(station);
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
	}
	
	private boolean isThereValidSchedule(){
		return currentSchedule != null;
	}
	
	public void adjustGenerators(){
		if(isThereValidSchedule()){
			getTimeAndAdjustGenerators();
		}
	}
	
	private void getTimeAndAdjustGenerators(){
		LocalTime currentTime = simulation.getTimeInSimulation();
		controller.adjustGenerators(currentSchedule, currentTime);
	}
}
