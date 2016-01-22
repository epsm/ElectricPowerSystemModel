package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;

public class PowerStationGenerationScheduleTest {
	PowerStationGenerationSchedule stationSchedule;
	GeneratorGenerationSchedule schedule_1;
	GeneratorGenerationSchedule schedule_2;
	LoadCurve curve;
	
	@Before
	public void setUp(){
		stationSchedule = new PowerStationGenerationSchedule(
				77, LocalDateTime.of(1, 2, 3, 4, 5, 6, 7), LocalDateTime.of(1, 2, 3, 4, 5), 2);
		curve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		schedule_1 = new GeneratorGenerationSchedule(99, false, true, null);
		schedule_2 = new GeneratorGenerationSchedule(88, true, false, curve);
		
		stationSchedule.addGeneratorSchedule(schedule_1);
		stationSchedule.addGeneratorSchedule(schedule_2);
	}
	
	@Test
	public void toStringWorksCorrect(){
		String expected = "<Pow.St.Gen.Sch. for st.#77"
				+ " generators schedules:"
				+ " [<Generator#88, turnedOn: true, astatic regulation: false,"
				+ " gen.curve:"
				+ " <LoadCurve: load in MW on day by hours, starts on 00.00:"
				+ " [64.88, 59.54, 55.72, 51.9, 48.47, 48.85,"
				+ " 48.09, 57.25, 76.35, 91.6, 100.0, 99.23,"
				+ " 91.6, 91.6, 91.22, 90.83, 90.83, 90.83,"
				+ " 90.83, 90.83, 90.83, 90.83, 90.83, 83.96]>>"
				+ "<Generator#99, turnedOn: false, astatic regulation: true,"
				+ " gen.curve: null>]>";
		String toStringResult = stationSchedule.toString();
		
		Assert.assertEquals(expected, toStringResult);
	}
}
