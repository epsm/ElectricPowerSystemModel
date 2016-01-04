package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public class PowerStationParameters extends Parameters{
	private MessageInclusionsContainer<GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp,	int quantityOfGeneratorParameters) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		generatorParameters = new MessageInclusionsContainer<GeneratorParameters>(
				quantityOfGeneratorParameters);
	}

	public void addGeneratorParameters(GeneratorParameters parameters){
		generatorParameters.addInclusion(parameters);
	}
	
	public GeneratorParameters getGeneratorParameters(int number){
		return generatorParameters.getInclusion(number);
	}
	
	public Set<Integer> getGeneratorParametersNumbers(){
		return generatorParameters.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return generatorParameters.getQuantityOfInclusions();
	}
	
	@Override
	public String toString() {
		return "PowerStationParameters toString() stub";
	}
}
