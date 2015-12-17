package main.java.com.yvhobby.epsm.model.generation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PowerStation{
	private int id;
	private Map<Integer, Generator> generators = new HashMap<Integer, Generator>();

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

	public void setId(int stationId) {
		id = stationId;
	}
}
