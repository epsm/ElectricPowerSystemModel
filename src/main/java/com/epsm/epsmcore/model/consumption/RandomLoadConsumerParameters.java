package com.epsm.epsmcore.model.consumption;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RandomLoadConsumerParameters extends ConsumerParameters {

	private final int maxWorkDurationInSeconds;
	private final int maxPauseBetweenWorkInSeconds;
	private final float maxLoad;

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
