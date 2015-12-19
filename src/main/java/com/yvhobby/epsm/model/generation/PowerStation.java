package main.java.com.yvhobby.epsm.model.generation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.java.com.yvhobby.epsm.model.dispatch.GeneratorParameters;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;

public class PowerStation{
	private int number;//must not be changed after creation
	private Map<Integer, Generator> generators = new HashMap<Integer, Generator>();
	private PowerStationParameters stationParameters;
	private Map<Integer, GeneratorParameters> generatorParameters =
			new HashMap<Integer, GeneratorParameters>();
	
	public PowerStation(int number) {
		this.number = number;
	}

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
			int generatorNumber = generator.getNumber();
			GeneratorParameters parameters = getGeneratorParameters(generator);
			generatorParameters.put(generatorNumber, parameters);
		}
	}
	
	private GeneratorParameters getGeneratorParameters(Generator generator){
		int generatorNumber = generator.getNumber();
		float minimalPower = generator.getMinimalTechnologyPower();
		float nominalPower = generator.getNominalPowerInMW(); 
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
	}
	
	public void createStationParametersReport(){
		stationParameters = new PowerStationParameters(number, generatorParameters);
	}
	
	public void addGenerator(Generator generator){
		int generatorNumber = generator.getNumber();
		Generator existGenerator = generators.put(generatorNumber, generator);
		
		if(existGenerator != null){
			String message = "Generator with nunber " + generatorNumber + " already installed";
			throw new PowerStationException(message);
		}
	}
	
	public Collection<Generator> getGenerators(){
		return generators.values();
	}

	public int getNumber(){
		return number;
	}
}
