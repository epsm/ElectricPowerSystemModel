package com.epsm.electricPowerSystemModel.model.generalModel;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Constants {
	public final static int NANOS_IN_SECOND = 1_000_000_000;
	public final static long NANOS_IN_HOUR = 60 * 60 * 1_000_000_000L;
	public final static long NANOS_IN_DAY = 24 * 60 * 60 * 1_000_000_000L;
	public final static float STANDART_FREQUENCY = 50;
	public static final int CONNECTION_TIMEOUT_IN_SECONDS = 10;
	public static final int PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS = 2;
	public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
}
