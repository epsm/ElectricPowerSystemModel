package com.epsm.electricPowerSystemModel.model.dispatch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

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
