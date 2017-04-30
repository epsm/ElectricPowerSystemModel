package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.State;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PowerStationState extends State {
	
	private List<GeneratorState> states = new ArrayList<>();
	private float frequency;
	
	public PowerStationState(long powerObjectId, LocalDateTime simulationTimeStamp, float frequency) {
		super(powerObjectId, simulationTimeStamp);

		this.frequency = frequency;
	}
}
