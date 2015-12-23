package main.java.com.epsm.electricPowerSystemModel.model.generation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;

public class PowerStation{
	private int number;//must not be changed after creation
	private Map<Integer, Generator> generators;
	private PowerStationParameters stationParameters;
	private Map<Integer, GeneratorParameters> generatorParameters;
	private Generator generator;
	private Logger logger = (Logger) LoggerFactory.getLogger(PowerStation.class);
	
	public PowerStation(int number) {
		this.number = number;
		generators = new HashMap<Integer, Generator>();
		
		logger.info("Power station was created.");
	}

	public float getCurrentGenerationInMW(){
		float generationInMW = 0;
		
		for(Generator generator: generators.values()){
			generationInMW += generator.calculateGeneration();
		}
		
		return generationInMW;
	}
	
	public PowerStationParameters getPowerStationParameters(){
		createNewContainerForGeneratorsParameters();
		createAndSaveParametersForEveryGenerator();
		createStationParametersReport();

		return stationParameters;
	}
	
	private void createNewContainerForGeneratorsParameters(){
		generatorParameters = new HashMap<Integer, GeneratorParameters>();
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
		this.generator = generator;
		verifyIsGeneratorNotNull();
		verifyIfGeneartorWithTheSameNumberExists();
		addGenerator();
	}
	
	private void verifyIsGeneratorNotNull(){
		if(generator == null){
			String message = "Generator must not be null.";
			throw new PowerStationException(message);
		}
	}
	
	private void verifyIfGeneartorWithTheSameNumberExists(){
		int generatorNumber = generator.getNumber();
		Generator existingGenerator = generators.get(generatorNumber);
		
		if(existingGenerator != null){
			String message = "Generator with number " + generatorNumber + " already installed.";
			throw new PowerStationException(message);
		}
	}
	
	private void addGenerator(){
		int generatorNumber = generator.getNumber();
		generators.put(generatorNumber, generator);
	}
	
	public Generator getGenerator(int generatorNumber){
		return generators.get(generatorNumber);
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return generators.keySet();
	}

	public int getNumber(){
		return number;
	}
}
