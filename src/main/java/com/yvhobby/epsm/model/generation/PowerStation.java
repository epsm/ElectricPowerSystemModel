package main.java.com.yvhobby.epsm.model.generation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import main.java.com.yvhobby.epsm.model.dispatch.GeneratorParameters;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;

public class PowerStation{
	private int id;
	private Map<Integer, Generator> generators = new HashMap<Integer, Generator>();
	private PowerStationParameters stationParameters;
	private HashSet<GeneratorParameters> generatorParameters = new HashSet<GeneratorParameters>();

	public float getCurrentGenerationInMW(){
		float generationInMW = 0;
		
		for(Generator generator: generators.values()){
			generationInMW += generator.getGenerationInMW();
		}
		
		return generationInMW;
	}
	
	public PowerStationParameters getPowerStationParameters(){
		clearPreviousParameters();
		createAndSaveParametersForEveryGenerator();
		createStationParametersReport();

		return stationParameters;
	}
	
	private void clearPreviousParameters(){
		generatorParameters.clear();
	}
	
	private void createAndSaveParametersForEveryGenerator(){
		for(Generator generator: generators.values()){
			generatorParameters.add(getGeneratorParameters(generator));
		}
	}
	
	private GeneratorParameters getGeneratorParameters(Generator generator){
		int generatorId = generator.getId();
		float minimalPower = generator.getMinimalTechnologyPower();
		float nominalPower = generator.getNominalPowerInMW(); 
		
		return new GeneratorParameters(generatorId, nominalPower, minimalPower);
	}
	
	public void createStationParametersReport(){
		stationParameters = new PowerStationParameters(id, generatorParameters);
	}
	
	public void addGenerator(Generator generator){
		int generatorId = generator.getId();
		Generator existGenerator = generators.put(generatorId, generator);
		
		if(existGenerator != null){
			String message = "Generator with id " + generatorId + " already installed";
			throw new PowerStationException(message);
		}
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
