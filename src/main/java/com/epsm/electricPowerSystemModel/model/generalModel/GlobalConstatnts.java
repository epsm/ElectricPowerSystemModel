package main.java.com.epsm.electricPowerSystemModel.model.generalModel;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GlobalConstatnts {
	public final static int NANOS_IN_SECOND = 1_000_000_000;
	public final static long NANOS_IN_HOUR = 60 * 60 * 1_000_000_000L;
	public final static long NANOS_IN_DAY = 24 * 60 * 60 * 1_000_000_000L;
	
	public final static float STANDART_FREQUENCY = 50;
	
	public static final int PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS = 1000;
	
	public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
}
