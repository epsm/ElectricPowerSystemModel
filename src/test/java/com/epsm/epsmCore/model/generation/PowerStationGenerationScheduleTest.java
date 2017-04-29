package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerCurve;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;

public class PowerStationGenerationScheduleTest {
	private PowerStationGenerationSchedule stationSchedule;
	private GeneratorGenerationSchedule schedule_1;
	private GeneratorGenerationSchedule schedule_2;
	private PowerCurve curve;
	private final long POWER_OBJECT_ID = 77;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(1, 2, 3, 4, 5);
	private final int QUANTITY_OF_GENERATORS = 2;
	private final int FIRST_GENERATOR_NUMBER = 99;
	private final int SECOND_GENERATOR_NUMBER = 88;
	private final boolean GENERATOR_ON = true;
	private final boolean GENERATOR_OFF = false;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	private final PowerCurve NULL_CURVE = null;
	
	@Before
	public void setUp(){
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID, 
				REAL_TIMESTAMP, SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS);
		
		curve = new PowerCurve(TestsConstants.LOAD_BY_HOURS);
		
		schedule_1 = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, 
				GENERATOR_OFF, ASTATIC_REGULATION_ON, NULL_CURVE);
		schedule_2 = new GeneratorGenerationSchedule(SECOND_GENERATOR_NUMBER, 
				GENERATOR_ON, ASTATIC_REGULATION_OFF, curve);
		
		stationSchedule.addGeneratorSchedule(schedule_1);
		stationSchedule.addGeneratorSchedule(schedule_2);
	}
	
	@Test
	public void toStringWorksCorrect(){
		String expected = "<Pow.St.Gen.Sch. for st.#77"
				+ " generators schedules:"
				+ " [<Generator#88, turnedOn: true, astatic regulation: false,"
				+ " gen.curve:"
				+ " <PowerCurve: load in MW on day by hours, starts on 00.00:"
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
