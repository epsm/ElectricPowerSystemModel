package main.java.com.yvhobby.epsm.model.generation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public class PowerStation{
	
	private ElectricPowerSystemSimulation simulation;
	private int id;
	private Map<Integer, Generator> generators = new HashMap<Integer, Generator>();

	public PowerStation(ElectricPowerSystemSimulation simulation, int id) {
		this.simulation = simulation;
		this.id = id;
	}

	public float getCurrentGenerationInMW(){
		float generationInMW = 0;
		
		for(Generator generator: generators.values()){
			generationInMW += generator.getGenerationInMW();
		}
		
		return generationInMW;
	}
	
	public void addGenerator(Generator generator){
		int generatorId = generator.getId();
		generators.put(generatorId, generator);
	}
	
	public Collection<Generator> getGenerators(){
		return generators.values();
	}

	public int getId() {
		return id;
	}
}
