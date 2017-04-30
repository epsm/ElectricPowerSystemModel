package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class GeneratorGenerationSchedule {

	private final int generatorNumber;
	private final boolean generatorTurnedOn;
	private final boolean astaticRegulatorTurnedOn;
	private final PowerCurve generationCurve;
}
