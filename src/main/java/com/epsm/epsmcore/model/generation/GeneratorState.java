package com.epsm.epsmcore.model.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class GeneratorState {

	private final int generatorNumber;
	private final float generationInWM;
}
