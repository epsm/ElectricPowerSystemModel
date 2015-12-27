package com.epsm.electricPowerSystemModel.model.generalModel;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GlobalConstants {
	public final static int NANOS_IN_SECOND = 1_000_000_000;
	public final static long NANOS_IN_HOUR = 60 * 60 * 1_000_000_000L;
	public final static long NANOS_IN_DAY = 24 * 60 * 60 * 1_000_000_000L;
	
	public final static float STANDART_FREQUENCY = 50;
	
	public static final int PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS = 1000;
	
	public static final int ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS = 10;
	public static final int PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS = 10;
	
	public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
}
