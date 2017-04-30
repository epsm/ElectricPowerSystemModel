package com.epsm.epsmcore.model.consumption;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@Getter
public class ScheduledLoadConsumerParameters extends ConsumerParameters {

	private final List<Float> approximateLoadByHoursOnDayInPercent;
	private final float maxLoadWithoutFluctuationsInMW;
	private final float randomFluctuationsInPercent;

	public ScheduledLoadConsumerParameters(
			long powerObjectId,
			float degreeOnDependingOfFrequency,
			List<Float> approximateLoadByHoursOnDayInPercent,
			float maxLoadWithoutFluctuationsInMW,
			float randomFluctuationsInPercent) {

		super(powerObjectId, ConsumerType.SCHEDULED_LOAD, degreeOnDependingOfFrequency);

		this.approximateLoadByHoursOnDayInPercent = Collections.unmodifiableList(approximateLoadByHoursOnDayInPercent);
		this.maxLoadWithoutFluctuationsInMW = maxLoadWithoutFluctuationsInMW;
		this.randomFluctuationsInPercent = randomFluctuationsInPercent;
	}
}
