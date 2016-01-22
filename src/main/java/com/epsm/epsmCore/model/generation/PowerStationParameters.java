package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Set;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.utils.json.PowerStationParametersJsonDeserializer;
import com.epsm.epsmCore.model.utils.json.PowerStationParametersJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PowerStationParametersJsonSerializer.class)
@JsonDeserialize(using = PowerStationParametersJsonDeserializer.class)
public class PowerStationParameters extends Parameters{
	private MessageInclusionsContainer<GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(long powerObjectId, LocalDateTime realTimeStamp,
			LocalDateTime simulationTimeStamp, int quantityOfGeneratorParameters) {
		
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
		return String.format("PowerStationParameters#%d toString() stub", powerObjectId);
	}
}
