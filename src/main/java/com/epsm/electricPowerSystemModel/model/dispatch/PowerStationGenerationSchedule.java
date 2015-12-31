package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

public class PowerStationGenerationSchedule extends DispatcherMessage{
	
	private GeneratorGenerationSchedule generatorSchedule;
	
	public  PowerStationGenerationSchedule(long powerStationId){
		super(powerStationId);
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
			throw new GenerationException(message);
		}
	}
	
	private void verifyIfScheduleWithTheSameGeneratorNumberExists(){
		int generatorNumber = generatorSchedule.getGeneratorNumber();
		GeneratorGenerationSchedule existingSchedule = getGeneratorGenerationSchedule(generatorNumber);
		
		if(existingSchedule != null){
			String message = "Generation schedule for generator number "
					+ generatorSchedule.getGeneratorNumber() + " already exists.";
			throw new GenerationException(message);
		}
	}
	
	private void addSchedule(){
		int generatorNumber = generatorSchedule.getGeneratorNumber();
		generatorSchedules.put(generatorNumber, generatorSchedule);
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

	@Override
	public String toString() {
		return "PowerStationGenerationSchedule toString() stub";
	}
}