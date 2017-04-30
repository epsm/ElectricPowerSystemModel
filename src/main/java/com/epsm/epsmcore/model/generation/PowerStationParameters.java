package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.Parameters;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PowerStationParameters extends Parameters {
	
	private List<GeneratorParameters> generatorParameters =  new ArrayList<>();
	
	public PowerStationParameters(long powerObjectId) {
		super(powerObjectId);
	}
}
