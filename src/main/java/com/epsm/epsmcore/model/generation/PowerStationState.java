package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@NoArgsConstructor
public class PowerStationState extends State {
	
	private Map<Integer, GeneratorState> states = new HashMap<>();
	private float frequency;
	
	public PowerStationState(long powerObjectId, LocalDateTime simulationTimeStamp, float frequency) {
		super(powerObjectId, simulationTimeStamp);

		this.frequency = frequency;
	}

	public GeneratorState getState(int generatorNumber) {
		return states.get(generatorNumber);
	}
}
