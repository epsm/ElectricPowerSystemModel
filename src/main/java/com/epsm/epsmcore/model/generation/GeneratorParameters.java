package com.epsm.epsmcore.model.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GeneratorParameters {

	private final int generatorNumber;
	private final float nominalPowerInMW;
	private final float minimalTechnologyPower;
}
