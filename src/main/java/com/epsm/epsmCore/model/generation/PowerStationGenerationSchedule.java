package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Set;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.utils.json.PowerStationGenerationScheduleJsonDeserializer;
import com.epsm.epsmCore.model.utils.json.PowerStationGenerationScheduleJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PowerStationGenerationScheduleJsonSerializer.class)
@JsonDeserialize(using = PowerStationGenerationScheduleJsonDeserializer.class)
public class PowerStationGenerationSchedule extends Command{
	private MessageInclusionsContainer<GeneratorGenerationSchedule> schedules;
	
	public PowerStationGenerationSchedule(long powerObjectId, LocalDateTime realTimeStamp,
			LocalDateTime simulationTimeStamp, int quanityOfGenerators) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		schedules = new MessageInclusionsContainer<GeneratorGenerationSchedule>(
				quanityOfGenerators);
	}

	public void addGeneratorSchedule(GeneratorGenerationSchedule schedule){
		schedules.addInclusion(schedule);
	}
	
	public GeneratorGenerationSchedule getGeneratorSchedule(int generatorNumber){
		return schedules.getInclusion(generatorNumber);
	}
	
	public Set<Integer> getGeneratorsNumbers(){
		return schedules.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return schedules.getQuantityOfInclusions();
	}
	
	@Override
	public String toString(){
		stringBuilder.setLength(0);
		stringBuilder.append("<Pow.St.Gen.Sch. for st.#");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" generators schedules: [");
		stringBuilder.append(schedules.toString());
		stringBuilder.append("]>");
		
		return stringBuilder.toString();
	}
}