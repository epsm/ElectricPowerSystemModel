package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.State;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConsumerState extends State {
	
	private float loadInMW;
	
	public ConsumerState(long powerObjectId, LocalDateTime simulationTimeStamp, float loadInMW) {
		
		super(powerObjectId, simulationTimeStamp);
		this.loadInMW = loadInMW;
	}
}
