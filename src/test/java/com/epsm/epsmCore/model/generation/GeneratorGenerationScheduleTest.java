package com.epsm.epsmCore.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;

public class GeneratorGenerationScheduleTest {
	GeneratorGenerationSchedule schedule;
	LoadCurve curve;
	
	@Before
	public void setUp(){
		curve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		schedule = new GeneratorGenerationSchedule(88, true, false, curve);
	}
	
	@Test
	public void toStringWorksCorrect(){
		String expected = "<Generator#88, turnedOn: true, astatic regulation: false,"
				+ " gen.curve: <LoadCurve: load in MW on day by hours, starts on 00.00: "
				+ "[64.88, 59.54, 55.72, 51.9, 48.47, 48.85,"
				+ " 48.09, 57.25, 76.35, 91.6, 100.0, 99.23,"
				+ " 91.6, 91.6, 91.22, 90.83, 90.83, 90.83,"
				+ " 90.83, 90.83, 90.83, 90.83, 90.83, 83.96]>>";
		String toStringResult = schedule.toString();
		
		Assert.assertEquals(expected, toStringResult);
	}
}
