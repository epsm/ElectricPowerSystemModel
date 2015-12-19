package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class PowerStationGenerationSchedule {
	private Map<Integer, GeneratorGenerationSchedule> generatorSchedules = 
			new HashMap<Integer, GeneratorGenerationSchedule>();
	GeneratorGenerationSchedule generatorSchedule;
	
	public void addGeneratorGenerationSchedule(GeneratorGenerationSchedule generatorSchedule){
		this.generatorSchedule = generatorSchedule;
		verifyScheduleOnNull();
		verifyIfScheduleWithTheSameGeneratorNumberExists();
		addSchedule();
	}
	
	private void verifyScheduleOnNull(){
		if(generatorSchedule == null){
			String message = "Generation schedule must not be null.";
			throw new PowerStationException(message);
		}
	}
	
	private void verifyIfScheduleWithTheSameGeneratorNumberExists(){
		int generatorNumber = generatorSchedule.getGeneratorNumber();
		GeneratorGenerationSchedule schedule = getGeneratorGenerationSchedule(generatorNumber);
		
		if(schedule != null){
			String message = "Generation schedule for generator number "
					+ generatorSchedule.getGeneratorNumber() + " already exists.";
			throw new PowerStationException(message);
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
}