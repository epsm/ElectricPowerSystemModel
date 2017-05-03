package com.epsm.epsmcore.model.consumption;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class RandomLoadConsumerParameters extends ConsumerParameters {

	private int maxWorkDurationInSeconds;
	private int maxPauseBetweenWorkInSeconds;
	private float maxLoad;

	public RandomLoadConsumerParameters(
			long powerObjectId,
			int maxWorkDurationInSeconds,
			int maxPauseBetweenWorkInSeconds,
			float maxLoadInMW,
			float degreeOfDependingOnFrequency) {

		super(powerObjectId, ConsumerType.RANDOM_LOAD, degreeOfDependingOnFrequency);

		this.maxWorkDurationInSeconds = maxWorkDurationInSeconds;
		this.maxPauseBetweenWorkInSeconds = maxPauseBetweenWorkInSeconds;
		this.maxLoad = maxLoadInMW;
	}
}
