package com.epsm.electricPowerSystemModel.model.generation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PoweStationParametersTest {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfgeneratorsParametersIsNullInConstructor(){
		expectedEx.expect(GenerationException.class);
		expectedEx.expectMessage("PowerStationParameters constructor: generatorsParameters can't be null.");
		
		new PowerStationParameters(0, null);
	}
}
