package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class ConsumerState extends State {
	
	private float loadInMW;
	private ConsumerType consumerType;
	
	public ConsumerState(long powerObjectId, LocalDateTime simulationTimeStamp, float loadInMW, ConsumerType consumerType) {
		
		super(powerObjectId, simulationTimeStamp);
		this.loadInMW = loadInMW;
		this.consumerType = consumerType;
	}
}
