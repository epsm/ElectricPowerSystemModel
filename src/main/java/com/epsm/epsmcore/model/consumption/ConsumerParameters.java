package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.Parameters;
import lombok.Getter;

@Getter
public abstract class ConsumerParameters extends Parameters {

	protected final ConsumerType consumerType;
	protected final float degreeOnDependingOfFrequency;

	public ConsumerParameters(long powerObjectId, ConsumerType consumerType, float degreeOnDependingOfFrequency) {
		super(powerObjectId);
		this.consumerType = consumerType;
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}
}
