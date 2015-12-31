package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

public class MainControlPanel extends PowerObject{
	private PowerStation station;
	private GeneratorsController controller;
	private PowerStationGenerationSchedule curentSchedule;
	private PowerStationGenerationSchedule receivedSchedule;
	private GenerationScheduleValidator validator;
	private PowerStationParameters parameters;
	private Logger logger;

	public MainControlPanel(ElectricPowerSystemSimulation simulation, TimeService timeService, 
			Dispatcher dispatcher, PowerStation station) {
		
		super(simulation, timeService, dispatcher);
		station.setMainControlPanel(this);
		station.setSimulation(simulation);
		this.station = station;
		controller = new GeneratorsController(station);
		validator = new GenerationScheduleValidator();
		logger = LoggerFactory.getLogger(MainControlPanelTest.class);
	}
	
	@Override
	public void processDispatcherMessage(Message message) {
		PowerStationGenerationSchedule schedule = (PowerStationGenerationSchedule) message;
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
			parameters = station.getPowerStationParameters();
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
	
	@Override
	public Message getState() {
		return station.getState();
	}
	
	public void adjustGenerators(){
		if(isThereValidSchedule()){
			getTimeAndAdjustGenerators();
		}
	}
	
	private void getTimeAndAdjustGenerators(){
		LocalTime currentTime = simulation.getTimeInSimulation();
		controller.adjustGenerators(curentSchedule, currentTime);
	}

	@Override
	public Message getParameters() {
		return station.getPowerStationParameters();
	}
}
