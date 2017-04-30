package com.epsm.epsmcore.model.consumption;

import lombok.Getter;

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
			float degreeOnDependingOfFrequency) {

		super(powerObjectId, ConsumerType.RANDOM_LOAD, degreeOnDependingOfFrequency);

		this.maxWorkDurationInSeconds = maxWorkDurationInSeconds;
		this.maxPauseBetweenWorkInSeconds = maxPauseBetweenWorkInSeconds;
		this.maxLoad = maxLoadInMW;
	}
}
