package com.epsm.epsmCore.model.generalModel;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Constants {
	public static final int NANOS_IN_SECOND = 1_000_000_000;
	public static final long NANOS_IN_HOUR = 60 * 60 * 1_000_000_000L;
	public static final long NANOS_IN_DAY = 24 * 60 * 60 * 1_000_000_000L;
	public static final float STANDART_FREQUENCY = 50;
	public static final int PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS = 2;
	/*
	 * for current realization SIMULATION_STEP_IN_NANOS must be that, to hit every exact
	 * 10 minutes (like 09:40:00.000000000). Otherwise States won't be correctly or even
	 * at all send to dispatcher. See calculatePowerBalance() in PowerObject class.
	 */
	public static final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	public static final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 1;
	public static final int PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS = 500;
	public static final int LOG_EVERY_SIMULATION_STEPS_DONE = 100;
	public static final int LOG_EVERY_REALTIME_STEPS_DONE = 20;
	public static final float TIME_CONASTNT = 2_000;
	public static final float ACCEPTABLE_FREQUENCY_DELTA = 0.2f;
	public static final int DETERMINED_HOURS_IN_DAY = 24;
	public static final float ASTATIC_REGULATION_DEAD_ZONE = 0.005f;
	public static final float STATIC_REGULATOR_COEFFICIENT_OF_STATISM = 0.06f;
	
	public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
}
