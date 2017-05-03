package com.epsm.epsmcore.model.consumption;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class ScheduledLoadConsumerParameters extends ConsumerParameters {

	private List<Float> approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;

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
