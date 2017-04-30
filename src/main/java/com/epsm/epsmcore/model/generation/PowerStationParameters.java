package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.Parameters;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
public class PowerStationParameters extends Parameters {
	
	private Map<Integer, GeneratorParameters> generatorParameters =  new HashMap<>();
	
	public PowerStationParameters(long powerObjectId) {
		super(powerObjectId);
	}

	public GeneratorParameters getGenerator(int generatorNumber) {
		return generatorParameters.get(generatorNumber);
	}
}
