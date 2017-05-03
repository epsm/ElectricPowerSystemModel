package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorGenerationSchedule {

	private int generatorNumber;
	private boolean generatorTurnedOn;
	private boolean astaticRegulatorTurnedOn;
	private PowerCurve generationCurve;
}
