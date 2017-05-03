package com.epsm.epsmcore.model.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorParameters {

	private int generatorNumber;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
}
