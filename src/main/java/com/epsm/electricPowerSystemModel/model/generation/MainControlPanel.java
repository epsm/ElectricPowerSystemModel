package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.Message;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class MainControlPanel{
	private ElectricPowerSystemSimulation sumulation;
	private PowerStation station;
	private GeneratorsController controller;
	private PowerStationGenerationSchedule curentSchedule;
	private PowerStationGenerationSchedule receivedSchedule;
	private GenerationScheduleValidator validator;
	private PowerStationParameters parameters;
	private Logger logger;

	public MainControlPanel(ElectricPowerSystemSimulation simulation, PowerStation station){
		this.station = station;
		controller = new GeneratorsController(station);
		validator = new GenerationScheduleValidator();
		logger = LoggerFactory.getLogger(MainControlPanelTest.class);
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
		if(parameters == null){
			parameters = (PowerStationParameters) station.getParameters();
		}
	}
	
	private boolean isReceivedScheduleValid(){
		try{
			validator.validate(receivedSchedule, parameters);
			return true;
		}catch (GenerationException exception){
			//TODO send request to dispatcher
			logger.warn("Received ", exception);
			return false;
		}
	}
	
	private void replaceCurrentSchedule(){
		curentSchedule = receivedSchedule;
	}
	
	private boolean isThereValidSchedule(){
		return curentSchedule != null;
	}
	
	public void adjustGenerators(){
		if(isThereValidSchedule()){
			getTimeAndAdjustGenerators();
		}
	}
	
	private void getTimeAndAdjustGenerators(){
		LocalTime currentTime = sumulation.getTimeInSimulation();
		controller.adjustGenerators(curentSchedule, currentTime);
	}
}
