package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.Parameters;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ConsumerParameters extends Parameters {

	protected ConsumerType consumerType;
	protected float degreeOnDependingOfFrequency;

	public ConsumerParameters(long powerObjectId, ConsumerType consumerType, float degreeOnDependingOfFrequency) {
		super(powerObjectId);
		this.consumerType = consumerType;
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}
}
