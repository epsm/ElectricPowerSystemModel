package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorGenerationScheduleTest {
	private GeneratorGenerationSchedule schedule;
	private PowerCurve curve;
	private final int GENERATOR_NUMBER = 88;
	private final boolean GENERATOR_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	
	@Before
	public void setUp(){
		curve = new PowerCurve(TestsConstants.LOAD_BY_HOURS);
		schedule = new GeneratorGenerationSchedule(GENERATOR_NUMBER, GENERATOR_ON,
				ASTATIC_REGULATION_OFF, curve);
	}
	
	@Test
	public void toStringWorksCorrect(){
		String expected = "<Generator#88, turnedOn: true, astatic regulation: false,"
				+ " gen.curve: <PowerCurve: load in MW on day by hours, starts on 00.00: "
				+ "[64.88, 59.54, 55.72, 51.9, 48.47, 48.85,"
				+ " 48.09, 57.25, 76.35, 91.6, 100.0, 99.23,"
				+ " 91.6, 91.6, 91.22, 90.83, 90.83, 90.83,"
				+ " 90.83, 90.83, 90.83, 90.83, 90.83, 83.96]>>";
		String toStringResult = schedule.toString();
		
		Assert.assertEquals(expected, toStringResult);
	}
}
