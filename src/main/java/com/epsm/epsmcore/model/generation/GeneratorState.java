package com.epsm.epsmcore.model.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorState {

	private int generatorNumber;
	private float generationInWM;
}
