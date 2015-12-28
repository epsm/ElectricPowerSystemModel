package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.generation.PowerStationException;

public class PowerStationGenerationSchedule extends DispatcherMessage{
	private long powerStationId;
	private Map<Integer, GeneratorGenerationSchedule> generatorSchedules 
			= new HashMap<Integer, GeneratorGenerationSchedule>();
	
	private GeneratorGenerationSchedule generatorSchedule;
	
	public  PowerStationGenerationSchedule(long powerStationId){
		super(LocalDateTime.now());
		this.powerStationId = powerStationId;
	}
	
	public void addGeneratorGenerationSchedule(GeneratorGenerationSchedule generatorSchedule){
		this.generatorSchedule = generatorSchedule;
		verifyIsScheduleNotNull();
		verifyIfScheduleWithTheSameGeneratorNumberExists();
		addSchedule();
	}
	
	private void verifyIsScheduleNotNull(){
		if(generatorSchedule == null){
			String message = "Generation schedule must not be null.";
			throw new PowerStationException(message);
		}
	}
	
	private void verifyIfScheduleWithTheSameGeneratorNumberExists(){
		int generatorNumber = generatorSchedule.getGeneratorNumber();
		GeneratorGenerationSchedule existingSchedule = getGeneratorGenerationSchedule(generatorNumber);
		
		if(existingSchedule != null){
			String message = "Generation schedule for generator number "
					+ generatorSchedule.getGeneratorNumber() + " already exists.";
			throw new PowerStationException(message);
		}
	}
	
	private void addSchedule(){
		int generatorNumber = generatorSchedule.getGeneratorNumber();
		generatorSchedules.put(generatorNumber, generatorSchedule);
	}
	
	public long getPowerStationId(){
		return powerStationId;
	}
	
	public GeneratorGenerationSchedule getGeneratorGenerationSchedule(int generatorNumber) {
		return generatorSchedules.get(generatorNumber);
	}
	
	public int getQuantityOfGenerators(){
		return generatorSchedules.size();
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return generatorSchedules.keySet();
	}
}