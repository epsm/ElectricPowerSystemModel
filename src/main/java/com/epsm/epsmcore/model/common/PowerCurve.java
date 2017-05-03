package com.epsm.epsmcore.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class PowerCurve {
	
	private List<Float> powerByHoursInMW;
}
