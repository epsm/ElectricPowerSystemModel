package com.epsm.epsmcore.model.consumption;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ScheduledLoadConsumerParameters extends ConsumerParameters {

	private final List<Float> approximateLoadByHoursOnDayInPercent;
	private final float maxLoadWithoutFluctuationsInMW;
	private final float randomFluctuationsInPercent;

	public ScheduledLoadConsumerParameters(
			long powerObjectId,
			ConsumerType consumerType,
			float degreeOnDependingOfFrequency,
			List<Float> approximateLoadByHoursOnDayInPercent,
			float maxLoadWithoutFluctuationsInMW,
			float randomFluctuationsInPercent) {

		super(powerObjectId, consumerType, degreeOnDependingOfFrequency);

		this.approximateLoadByHoursOnDayInPercent = Collections.unmodifiableList(approximateLoadByHoursOnDayInPercent);
		this.maxLoadWithoutFluctuationsInMW = maxLoadWithoutFluctuationsInMW;
		this.randomFluctuationsInPercent = randomFluctuationsInPercent;
	}
}
